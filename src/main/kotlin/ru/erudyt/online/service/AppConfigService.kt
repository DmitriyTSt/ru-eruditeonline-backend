package ru.erudyt.online.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.erudyt.online.dto.model.AppConfig
import ru.erudyt.online.dto.model.AppUpdate
import ru.erudyt.online.dto.model.AppVersion
import ru.erudyt.online.entity.fs.AppUpdateEntity
import ru.erudyt.online.mapper.AppVersionMapper
import ru.erudyt.online.repository.fs.AppConfigRepository

@Service
class AppConfigService @Autowired constructor(
    private val appConfigRepository: AppConfigRepository,
    private val appVersionMapper: AppVersionMapper,
) {

    fun getAppConfig(appVersionFull: String): AppConfig {
        return AppConfig(
            appUpdate = buildAppUpdate(
                appVersion = appVersionMapper.fromFullString(appVersionFull),
                appUpdateEntity = appConfigRepository.getAppUpdateConfig(),
            )
        )
    }

    private fun buildAppUpdate(appVersion: AppVersion, appUpdateEntity: AppUpdateEntity?): AppUpdate? {
        if (appUpdateEntity == null) return null

        val minForceUpdateVersionInt = appUpdateEntity.minForceUpdateVersion
            ?.let { appVersionMapper.fromShortString(it) }
            ?.versionInt
        if (minForceUpdateVersionInt != null && appVersion.versionInt < minForceUpdateVersionInt) {
            return AppUpdate(forceUpdate = true)
        }

        val minSoftUpdateVersionInt = appUpdateEntity.minSoftUpdateVersion
            ?.let { appVersionMapper.fromShortString(it) }
            ?.versionInt
        if (minSoftUpdateVersionInt != null && appVersion.versionInt < minSoftUpdateVersionInt) {
            return AppUpdate(forceUpdate = false)
        }

        return null
    }
}