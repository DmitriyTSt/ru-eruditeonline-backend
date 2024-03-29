package ru.erudyt.online.controller.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.erudyt.online.controller.base.BaseResponse
import ru.erudyt.online.controller.base.EmptyResponse
import ru.erudyt.online.dto.model.Token
import ru.erudyt.online.dto.request.DeviceRequest
import ru.erudyt.online.dto.request.LoginRequest
import ru.erudyt.online.dto.request.RefreshTokenRequest
import ru.erudyt.online.dto.request.RegistrationRequest
import ru.erudyt.online.dto.response.AnonymTokenResponse
import ru.erudyt.online.dto.response.LoginResponse
import ru.erudyt.online.dto.response.RefreshResponse
import ru.erudyt.online.service.AuthService

@RestController
@RequestMapping("/api/v1/auth", produces = [MediaType.APPLICATION_JSON_VALUE])
class AuthController @Autowired constructor(
    private val authService: AuthService,
) {

    @PostMapping("/anonym")
    fun createAnonym(@RequestBody request: DeviceRequest): ResponseEntity<BaseResponse<AnonymTokenResponse>> {
        return ResponseEntity.ok(BaseResponse(authService.createAnonym(request.device)))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<BaseResponse<LoginResponse>> {
        return ResponseEntity.ok(BaseResponse(authService.login(request.login, request.password)))
    }

    @GetMapping("/logout")
    fun logout(): ResponseEntity<BaseResponse<AnonymTokenResponse>> {
        return ResponseEntity.ok(BaseResponse(authService.logout()))
    }

    @PostMapping("/registration")
    fun registration(@RequestBody request: RegistrationRequest): ResponseEntity<EmptyResponse> {
        return ResponseEntity.ok(authService.registration(request))
    }

    @GetMapping("/confirm")
    fun confirmEmail(@RequestParam("token") token: String): ResponseEntity<EmptyResponse> {
        return ResponseEntity.ok(authService.confirmEmail(token))
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<BaseResponse<RefreshResponse>> {
        return ResponseEntity.ok(BaseResponse(authService.refreshToken(request.deviceId, request.refreshToken)))
    }
}