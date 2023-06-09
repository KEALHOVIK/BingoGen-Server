package dev.catsuperberg.bingogen.server.common

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
import com.typesafe.config.ConfigValueFactory
import dev.catsuperberg.bingogen.server.repository.DatabaseCredentials
import io.ktor.server.config.*
import java.io.File

object TestConfiguration {
    private const val testResourcesFolder = "src/test/resources"
    private const val configFile = "application.test.conf"
    private val testConfig: ApplicationConfig

    val databasesToTest: List<DatabaseCredentials>

    init {
        val configExists = javaClass.classLoader.getResource(configFile) != null
        testConfig = if (configExists) HoconApplicationConfig(ConfigFactory.load(configFile)) else initDefaultConfig()

        databasesToTest = testConfig.configList("test.databases-to-test")
            .map { it.property("value").getList() }
            .map(DatabaseCredentials::fromConfigStrings)
    }

    private fun initDefaultConfig(): ApplicationConfig {
        val defaultConfig = ConfigFactory.empty().withValue(
            "test.databases-to-test", ConfigValueFactory.fromIterable(
                listOf(
                    mapOf("value" to DatabaseCredentials.H2TEST.toConfigList())
                )
            )
        )

        val resources = File(testResourcesFolder)
        if (resources.exists().not())
            resources.mkdirs()
        val outputFile = File(testResourcesFolder, configFile)
        val renderOptions = ConfigRenderOptions.defaults().setOriginComments(false).setJson(false)
        outputFile.writeText(defaultConfig.root().render(renderOptions))
        return HoconApplicationConfig(defaultConfig)
    }

    private fun DatabaseCredentials.toConfigList() = listOf(url, driver, user, password)
}
