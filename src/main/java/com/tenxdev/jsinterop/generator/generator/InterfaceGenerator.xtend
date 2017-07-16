package com.tenxdev.jsinterop.generator.generator

import com.tenxdev.jsinterop.generator.model.DefinitionInfo
import com.tenxdev.jsinterop.generator.model.InterfaceDefinition
import com.tenxdev.jsinterop.generator.processing.TypeMapper

class InterfaceGenerator extends Template{

    def generate(String basePackageName, DefinitionInfo definitionInfo, TypeMapper typeMapper){
        var definition=definitionInfo.getDefinition() as InterfaceDefinition
        return '''
package «basePackageName»«definitionInfo.getPackgeName()»;

«IF !definition.methods.empty»import jsinterop.annotations.JsMethod;«ENDIF»
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

«FOR importName: definitionInfo.getImportedPackages»
import «if(importName.startsWith(".")) basePackageName else ""»«importName»;
«ENDFOR»

@JsType(namespace = JsPackage.GLOBAL, isNative = true)
public class «definition.getName»{
    «FOR method: definition.methods»

    @JsMethod(name = "«method.name»")
    public native «typeMapper.mapType(method.firstReturnType)» «method.callbackMethodName»(«
        FOR argument: method.arguments SEPARATOR ", "
        »«typeMapper.mapType(argument.types.get(0))» «argument.name»«ENDFOR»);
    «ENDFOR»

}


    '''
    }
}