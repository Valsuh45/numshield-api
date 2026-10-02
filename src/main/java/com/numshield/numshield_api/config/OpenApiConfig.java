package com.numshield.numshield_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI specification configuration for NumShield API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI numShieldOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NumShield API")
                        .description("Free initial v1 release for Cameroon mobile-number normalization, "
                                + "structural validation, and configured prefix-allocation lookup. "
                                + "No billing or paywall. Validity and operator results do not prove "
                                + "reachability or ownership. All phoneNumber JSON values must be strings. "
                                + "The response envelope replaces the pre-release unwrapped format; "
                                + "see README migration guidance before upgrading existing clients.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("NumShield Team"))
                        .license(new License()
                                .name("GNU General Public License v3.0")
                                .url("https://www.gnu.org/licenses/gpl-3.0.html")));
    }
}
