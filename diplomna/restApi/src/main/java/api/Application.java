package api;

import com.google.common.io.ByteStreams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

//@SpringBootApplication

@Controller
@EnableAutoConfiguration
@ComponentScan
public class Application extends WebMvcConfigurerAdapter {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {

      System.out.println("Let's inspect the beans provided by Spring Boot:");

      String[] beanNames = ctx.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (String beanName : beanNames) {
        System.out.println(beanName);
      }

    };
  }
  
  
//  @Bean
//  public WebMvcConfigurer webMvcConfigurer() {
//    return new WebMvcConfigurerAdapter() {
      @Override
      public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
            .addResourceLocations("/resources/");
      }
//    };
//  }


  @RequestMapping(value="/")
  public String welcomeGet(HttpServletResponse response) throws IOException {
    return "/login.html";
//    "https://elisaveta.accounts400.ondemand.com/saml2/idp/sso?sp=oac.accounts.sap.com&RelayState=https%3A%2F%2Felisaveta.accounts400.ondemand.com");
  }
    
  
}