// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.angular2.lang.html.lexer;

import com.intellij.lexer.EmbeddedTokenTypesProvider;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.extensions.ExtensionPoint;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.extensions.ExtensionsArea;
import com.intellij.testFramework.LexerTestCase;
import org.angularjs.AngularTestUtil;
import org.jetbrains.annotations.NonNls;

import java.lang.reflect.Modifier;

public class Angular2HtmlLexerTest extends LexerTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    registerExtensionPoint(EmbeddedTokenTypesProvider.EXTENSION_POINT_NAME, EmbeddedTokenTypesProvider.class);
  }

  protected <T> void registerExtensionPoint(final ExtensionPointName<T> extensionPointName, final Class<T> aClass) {
    registerExtensionPoint(Extensions.getRootArea(), extensionPointName, aClass);
  }

  protected <T> void registerExtensionPoint(final ExtensionsArea area, final ExtensionPointName<T> extensionPointName,
                                            final Class<? extends T> aClass) {
    final String name = extensionPointName.getName();
    if (!area.hasExtensionPoint(name)) {
      ExtensionPoint.Kind kind = aClass.isInterface() || (aClass.getModifiers() & Modifier.ABSTRACT) != 0
                                 ? ExtensionPoint.Kind.INTERFACE
                                 : ExtensionPoint.Kind.BEAN_CLASS;
      area.registerExtensionPoint(name, aClass.getName(), kind);
    }
  }

  public void testNoNewline() {
    doTest("<t>a</t>");
  }

  public void testNewlines() {
    doTest("<t\n>\r\na\r</t>");
  }

  public void testComments() {
    doTest("<!-- {{ v }} -->");
  }

  public void testInterpolation1() {
    doTest("<t a=\"{{v}}\" b=\"s{{m}}e\" c=\"s{{m//c}}e\">");
  }

  public void testInterpolation2() {
    doTest("{{ a }}b{{ c // comment }}");
  }

  public void testBoundAttributes() {
    doTest("<a [src]=bla() (click)='event()'></a>");
  }

  public void testMultipleInterpolations() {
    doTest("{{test}} !=bbb {{foo() - bar()}}");
  }

  public void testComplex() {
    doTest("<div *ngFor=\"let contact of value; index as i\"\n" +
           "  (click)=\"contact\"\n" +
           "</div>\n" +
           "\n" +
           "<li *ngFor=\"let user of userObservable | async as users; index as i; first as isFirst\">\n" +
           "  {{i}}/{{users.length}}. {{user}} <span *ngIf=\"isFirst\">default</span>\n" +
           "</li>\n" +
           "\n" +
           "<tr [style]=\"{'visible': con}\" *ngFor=\"let contact of contacts; index as i\">\n" +
           "  <td>{{i + 1}}</td>\n" +
           "</tr>\n");
  }

  @Override
  protected void doTest(@NonNls String text) {
    super.doTest(text);
    checkCorrectRestart(text);
  }

  @Override
  protected Lexer createLexer() {
    return new Angular2HtmlLexer(true, null);
  }

  @Override
  protected String getDirPath() {
    return AngularTestUtil.getBaseTestDataPath(getClass()).substring(PathManager.getHomePath().length());
  }
}
