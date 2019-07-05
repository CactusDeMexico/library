package com.pancarte.configuration;
import com.pancarte.controlleur.LibraryController;
import com.pancarte.proxy.MicroserviceLibraryProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MicroserviceLibraryProxy Library;
    public LibraryController Conf= new LibraryController();




    //todo:connextion
    //private final String USERS_QUERY =  Conf.UserSQL();

    //private final String ROLES_QUERY = Conf.RoleSQL();

   private final String USERS_QUERY = "select email, password, active from user_account where email=?";
    private final String ROLES_QUERY = "select u.email, r.role from user_account u inner join user_role ur on (u.id_user = ur.id_user) inner join role r on (ur.id_role=r.id_role) where u.email=?";
 // private final String USERS_QUERY = "";
    //private final String ROLES_QUERY = "";

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication()
                .usersByUsernameQuery(USERS_QUERY)
                .authoritiesByUsernameQuery(ROLES_QUERY)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()

                .antMatchers("/search/**").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/signup").permitAll()
                .antMatchers("/test").anonymous()
                .antMatchers("/borrow").hasAuthority("ADMIN")
                .antMatchers("/borrowed").hasAuthority("ADMIN")
                .antMatchers("/loggedhome").hasAuthority("ADMIN").anyRequest()

                .authenticated().and().csrf().disable()

                .formLogin().loginPage("/login").failureUrl("/login?error=true")
                .defaultSuccessUrl("/index")
                .defaultSuccessUrl("/loggedhome")
                .usernameParameter("email")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .and().rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60*60)
                .and().exceptionHandling().accessDeniedPage("/access_denied");
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(Conf.email).password(Conf.password);
    }
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);

        return db;
    }
}
