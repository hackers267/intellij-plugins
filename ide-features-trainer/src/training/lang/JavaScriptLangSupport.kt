// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package training.lang

import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import training.learn.LessonsBundle

class JavaScriptLangSupport : AbstractLangSupport() {

  companion object {
    @JvmStatic
    val lang: String = "JavaScript"
  }

  override val primaryLanguage: String
    get() = lang

  override val defaultProjectName: String = "LearnJavaScriptProject"

  override val defaultProductName: String = "WebStorm"

  override val langCourseFeedback: String = LessonsBundle.message("js.module.feedback")
  
  override fun applyToProjectAfterConfigure(): (Project) -> Unit = {
  }

  override fun checkSdk(sdk: Sdk?, project: Project) {}
}