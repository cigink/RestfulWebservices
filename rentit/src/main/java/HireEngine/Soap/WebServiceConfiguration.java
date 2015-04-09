/**
 * 
 */
package HireEngine.Soap;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import com.sun.xml.ws.transport.http.servlet.WSSpringServlet;

/**
 * @author Vinod Rockson
 *
 */

@Configuration
@ImportResource("classpath:META-INF/integration/applicationContext-ws.xml")
public class WebServiceConfiguration {

    @Bean
    public ServletRegistrationBean dispatcherServletWSServlet() {
        return new ServletRegistrationBean(new WSSpringServlet(), "/soap/*");
    }
    
}