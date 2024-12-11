package starter.core.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.stereotype.Component
import reactor.netty.http.Http3SslContextSpec
import reactor.netty.http.HttpProtocol
import reactor.netty.http.server.HttpServer
import reactor.netty.tcp.SslProvider.SslContextSpec
import java.time.Duration

@Component
class NettyHttp3Config: WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    @Value("\${app.http3.enabled}")
    private val http3Enabled: Boolean = false

    override fun customize(factory: NettyReactiveWebServerFactory) {
        if (!http3Enabled) return
        factory.addServerCustomizers(NettyServerCustomizer { server: HttpServer ->
            val sslBundle = factory.sslBundles.getBundle("server-http3")
            val sslContextSpec = Http3SslContextSpec.forServer(
                sslBundle.managers.keyManagerFactory,
                sslBundle.key.password
            )
            server // Configure HTTP/3 protocol
                .protocol(HttpProtocol.HTTP3)
                .secure { spec: SslContextSpec -> spec.sslContext(sslContextSpec) }
                .http3Settings { spec ->
                    spec
                        .idleTimeout(Duration.ofSeconds(5))
                        .maxData(10000000)
                        .maxStreamDataBidirectionalRemote(1000000)
                        .maxStreamsBidirectional(100)
                }
        })
    }
}