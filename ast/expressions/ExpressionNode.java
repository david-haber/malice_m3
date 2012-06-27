package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;
import ast.statements.StatementNode;

public abstract class ExpressionNode extends StatementNode {

   public abstract void acceptVisitor(Visitor v) throws SemanticException;

}
