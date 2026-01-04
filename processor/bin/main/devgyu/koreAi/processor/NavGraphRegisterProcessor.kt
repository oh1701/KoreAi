package devgyu.koreAi.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.validate
import devgyu.koreAi.annotation.NavGraphRegister


/**
 * NavGraph Sealed class 들의 composable 코드 자동 생성을 위한 Processor
 * [NavGraphRegister] 를 사용해야 정상작동한다
 *
 * @see devgyu.koreAi.annotation.NavGraphRegister
 * @see devgyu.koreAi.presentation.model.navigation.graph.KoreAiNavGraph
 * */
class NavGraphRegisterProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    private val packageName = "${projectPath}.presentation.navigation"
    private val fileAndFunctionName = "generateNavGraphComposable"
    private val navGraphComposableStringBuilder = StringBuilder()
    private val containingFileList = mutableListOf<KSFile>()

    override fun finish() {
        super.finish()

        navGraphComposableStringBuilder.ifEmpty { throw Exception("컴포저블 리스트가 비어있음") }

        val file = codeGenerator.createNewFile(
            Dependencies(true, sources = containingFileList.toTypedArray()),
            packageName,
            fileAndFunctionName
        )

        file.writer().use { writer ->
            writer.write(autoNavigationFunctionFileString(navGraphComposableStringBuilder))
        }
    }

    override fun onError() {
        super.onError()
        logger.error("NavGraphRegisterProcessor error")
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(annotationName)
        val symbolGroups = symbols
            .filterIsInstance<KSClassDeclaration>()
            .groupBy { it.validate() }

        val visitor = NavGraphRegisterVisitor(
            logger = logger,
            annotationName = annotationName,
            generateComposableString = { str ->
                if(navGraphComposableStringBuilder.isNotEmpty()){ navGraphComposableStringBuilder.append("\n\t") }
                navGraphComposableStringBuilder.append(str)
            }
        )

        symbolGroups[true]?.forEach { classDeclaration ->
            classDeclaration.containingFile?.let { it1 -> containingFileList.add(it1) }
            classDeclaration.accept(visitor, Unit)
        }

        return symbolGroups[false] ?: emptyList()
    }

    private fun autoNavigationFunctionFileString(navComposableListString: StringBuilder): String{
        return """
            |package $packageName
            |    
            |import androidx.navigation.NavController
            |import androidx.navigation.NavGraphBuilder
            |import androidx.compose.material3.SnackbarHostState
            |import ${projectPath}.presentation.model.navigation.*
            |import ${projectPath}.presentation.model.navigation.graph.*
            |
            |/**
            |* @see ${projectPath}.processor.NavGraphRegisterProcessor
            |*/
            |fun NavGraphBuilder.${fileAndFunctionName}(navController: NavController) {
            |    $navComposableListString
            |}
        """.trimMargin()
    }

    companion object {
        private val annotationName = NavGraphRegister::class.qualifiedName!! // 패키지명을 포함한 어노테이션 클래스 명
        private val projectPath = this::class.qualifiedName!!.split(".")
            .take(2)
            .joinToString(".")
    }
}