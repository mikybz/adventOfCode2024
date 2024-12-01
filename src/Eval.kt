import javax.script.*


object Eval {
    val engine = ScriptEngineManager().getEngineByExtension("kts")!!

    fun eval(script: String) = engine.eval(script)

    fun compile(script: String): CompiledScript = when (engine) {
        is Compilable -> engine.compile(script)
        else -> error("Engine can not compile")
    }
    fun run(compiled: CompiledScript) = compiled.eval()

    fun run(compiled: CompiledScript, bindings:Bindings) = compiled.eval(bindings)

}

/** In file: build.gradle.kts, it need the code below: */
//
//dependencies {
//    runtimeOnly("org.jetbrains.kotlin:kotlin-main-kts:1.7.22")
//    runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.7.22")
//    testRuntimeOnly("org.jetbrains.kotlin:kotlin-main-kts:1.7.22")
//    testRuntimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.7.22")
//}