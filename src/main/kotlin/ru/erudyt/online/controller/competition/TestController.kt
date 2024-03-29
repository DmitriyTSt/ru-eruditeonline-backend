package ru.erudyt.online.controller.competition

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.erudyt.online.controller.base.BaseResponse
import ru.erudyt.online.dto.enums.ApiError
import ru.erudyt.online.dto.enums.getException
import ru.erudyt.online.dto.request.CheckTestRequest
import ru.erudyt.online.dto.response.CheckTestResponse
import ru.erudyt.online.dto.response.CompetitionTestResponse
import ru.erudyt.online.entity.test.TestEntity
import ru.erudyt.online.service.TestService
import javax.servlet.http.HttpServletRequest
import ru.erudyt.online.config.property.BackendAppSettings

@RestController
@RequestMapping("/api/v1/competition/", produces = [MediaType.APPLICATION_JSON_VALUE])
@EnableConfigurationProperties(BackendAppSettings::class)
class TestController @Autowired constructor(
    private val testService: TestService,
    private val appSettings: BackendAppSettings,
) {

    @GetMapping("rawTestEntity/{code}")
    fun getRawTest(@PathVariable("code") code: String): ResponseEntity<BaseResponse<TestEntity>> {
        if (appSettings.env == BackendAppSettings.ENV_DEV) {
            return ResponseEntity.ok(BaseResponse(testService.getRawTest(code)))
        } else {
            throw ApiError.NOT_AVAILABLE_BY_ENV.getException()
        }
    }

    @GetMapping("/test/{id}")
    fun getTest(@PathVariable("id") id: String): ResponseEntity<BaseResponse<CompetitionTestResponse>> {
        return ResponseEntity.ok(BaseResponse(CompetitionTestResponse(testService.getTestForPassing(id))))
    }

    @PostMapping("/check")
    fun check(
        @RequestBody checkRequest: CheckTestRequest,
        httpRequest: HttpServletRequest
    ): ResponseEntity<BaseResponse<CheckTestResponse>> {
        return ResponseEntity.ok(BaseResponse(testService.check(checkRequest, httpRequest)))
    }
}