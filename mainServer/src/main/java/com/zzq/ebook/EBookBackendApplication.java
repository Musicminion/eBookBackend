package com.zzq.ebook;

import org.apache.catalina.Context;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EBookBackendApplication {
//
//    @Bean
//    public TomcatServletWebServerFactory tomcatServletWebServerFactory(Connector connector){
//        TomcatServletWebServerFactory tomcat=new TomcatServletWebServerFactory(){
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint securityConstraint=new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection=new SecurityCollection();
//                collection.addPattern("/*");
//
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(connector);
//        return tomcat;
//    }
//    @Bean
//    public Connector connector(){
//        Connector connector=new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        connector.setPort(8080);
//        // Set the secure connection flag that will be assigned to requests received through this connector.
//        // @param secure The new secure connection flag
//        // if connector.setSecure(true),the http use the http and https use the https;
//        // else if connector.setSecure(false),the http redirect to https;
//        connector.setSecure(false);
//        connector.setRedirectPort(8443);
//        connector.setAllowTrace(true);
//
//        return connector;
//    }

    public static void main(String[] args) {
        SpringApplication.run(EBookBackendApplication.class, args);
    }

}
