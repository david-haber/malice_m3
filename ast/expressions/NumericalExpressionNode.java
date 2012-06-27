package ast.expressions;

import semanticAnalyzer.IdentifierNotDeclaredException;
import semanticAnalyzer.SemanticException;
import semanticAnalyzer.TypeMatchException;
import visitor.Visitor;

public abstract class NumericalExpressionNode extends NonCharExpressionNode {

   public abstract void acceptVisitor(Visitor v) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException;

}
