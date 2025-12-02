package devgyu.koreAi.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Modifier

class NavGraphRegisterVisitor(
    private val logger: KSPLogger,
    private val annotationName: String,
    private val generateComposableString: (String) -> Unit
) : KSVisitorVoid() {
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit){
        // 실드 클래스에서만 사용할 수 있게 설정
        if(!classDeclaration.modifiers.contains(Modifier.SEALED)){
            logger.error("${annotationName}`s target is only Sealed [class or Interface]", classDeclaration)
            return
        }

        logger.warn("generate [${classDeclaration}]'s navComposable code")
        generateComposableString("KoreAiNavGraphComposable<${classDeclaration}>(navController)")
        logger.warn("generated code !!")
    }
}
