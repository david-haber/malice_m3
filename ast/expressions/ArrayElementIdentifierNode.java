package ast.expressions;

import semanticAnalyzer.IdentifierNotDeclaredException;
import semanticAnalyzer.SemanticException;
import semanticAnalyzer.TypeMatchException;
import visitor.Visitor;

public class ArrayElementIdentifierNode extends IdentifierNode {

   private BasicIdentifierNode arrayIdentifierNode; // E.g. Bob
   private NumericalExpressionNode arrayIndexNode; // E.g 2

   // Equivalent to Bob[2]

   public ArrayElementIdentifierNode(BasicIdentifierNode arrayIdentifierNode,
         NumericalExpressionNode arrayIndexNode) {
      this.arrayIdentifierNode = arrayIdentifierNode;
      this.arrayIndexNode = arrayIndexNode;
   }

   public BasicIdentifierNode getArrayIdentifierNode() {
      return arrayIdentifierNode;
   }

   public NumericalExpressionNode getArrayIndexNode() {
      return arrayIndexNode;
   }

   public void acceptVisitor(Visitor v) throws TypeMatchException,
         IdentifierNotDeclaredException, SemanticException {
      v.visitArrayElementIdentifierNode(arrayIdentifierNode, arrayIndexNode);
   }

   public String getName() {
      // TODO Auto-generated method stub
      return arrayIdentifierNode.getName();
   }

}
