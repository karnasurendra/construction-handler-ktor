import com.handler.workers.karna.JwtConfig
import com.handler.workers.karna.service.UserService
import com.handler.workers.karna.utils.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*


/**Authorized requests will go through this block, When any request is UnAuthorized,
 * With : InValid Token, Token Expiry, Invalid id, the requests will not process further
 * Here only it will stop and return the user with UnAuthorized message*/
fun Application.authenticate(userService: UserService) {
    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            realm = "ktor.io"
            validate { credential ->
                try {
                    val id = credential.payload.getClaim("id").asString()
                        ?: throw Throwable("Invalid token: Missing or empty 'id' claim")

                    val user = userService.findUserById(id)
                        ?: throw Throwable("User not found for ID: $id")

                    JWTPrincipal(credential.payload)
                } catch (e: Exception) {
                    null
                }
            }

            challenge { _, _ ->
                val response: Response<Nothing> =
                    Response(HttpStatusCode.Unauthorized.value, message = "UnAuthorized", error = "UnAuthorized")
                call.respond(response)
            }
        }
    }
}



