package eu.janbednar.camel.test;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class MySpringBootRouter extends RouteBuilder {

    @Override
    public void configure() {
        from("timer:hello?period={{timer.period}}").routeId("hello")
                .process(exchange -> exchange.getMessage().setBody(new HashMap<String, Object>() {{
                    put("timestamp", exchange.getProperty(Exchange.TIMER_FIRED_TIME));
                    put("counter", exchange.getProperty(Exchange.TIMER_COUNTER));
                }}))
                .to("elasticsearch-rest:local?operation=Index&hostAddresses=localhost:9200&indexName=test_timer")
                .to("log:result?showAll=true");
    }

}
