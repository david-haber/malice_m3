package ast.expressions;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;
import ast.basic.Node;

public abstract class BinaryNumericalExpressionNode extends
      NumericalExpressionNode implements BinaryExpressionNode, Node {

   private NumericalExpressionNode leftChild;
   private NumericalExpressionNode rightChild;

   // Constructor
   BinaryNumericalExpressionNode(NumericalExpressionNode leftChild,
         NumericalExpressionNode rightChild) {
      this.leftChild = leftChild;
      this.rightChild = rightChild;
   }

   public void setLeftChild(NumericalExpressionNode leftChild) {
      if (leftChild instanceof NumericalExpressionNode) {
         this.leftChild = (NumericalExpressionNode) leftChild;
      }
   }

   public void setRightChild(NumericalExpressionNode rightChild) {
      if (rightChild instanceof NumericalExpressionNode) {
         this.rightChild = (NumericalExpressionNode) rightChild;
      }
   }

   public NumericalExpressionNode getLeftChildExpression() {
      return leftChild;
   }

   public NumericalExpressionNode getRightChildExpression() {
      return rightChild;
   }

   public abstract void acceptVisitor(Visitor v) throws SemanticException;

}
