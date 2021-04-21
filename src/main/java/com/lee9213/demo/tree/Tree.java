package com.lee9213.demo.tree;

import sun.rmi.transport.proxy.CGIHandler;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author libo
 * @version 1.0
 * @date 2017/6/8 15:23
 */
public class Tree<T extends Comparable<T>> {

    private Node<T> root;
    private AtomicLong size = new AtomicLong(0);
    private volatile boolean overrideMode = true;

    public Tree(){
        this.root = new Node<T>();
    }

    public Tree(boolean overrideMode){
        this();
        this.overrideMode = overrideMode;
    }

    public T addNode(T value){
        Node<T> t = new Node<>(value);
        return addNode(t);
    }
    private T addNode(Node<T> node){
        node.setLeft(null);
        node.setRight(null);
        node.setColor(Color.RED);
        setParent(node,null);

        if(root.getLeft() == null){
            root.setLeft(node);
            node.setColor(Color.BLACK);
            size.incrementAndGet();
        }else{
            Node<T> x = findParentNode(node);
            int cmp = x.getValue().compareTo(node.getValue());

            if(this.overrideMode && cmp == 0){
                T v = x.getValue();
                x.setValue(node.getValue());
                return v;
            }else if(cmp == 0){
                return x.getValue();
            }

            setParent(node,x);

            if(cmp > 0){
                x.setLeft(node);
            }else{
                x.setRight(node);
            }

            fixInsert(node);

            size.incrementAndGet();
        }
        return null;
    }

    private void fixInsert(Node<T> node) {
        Node<T> parent = node.getParent();
        while(parent != null && Objects.equals(parent.getColor(),Color.RED)){
            Node<T> uncle = getUncle(node);
            if(uncle == null){
                Node<T> ancestor = parent.getParent();
                if(parent == ancestor.getLeft()){
                    boolean isRight = node == parent.getRight();
                    if(isRight){
                        rotateLeft(parent);
                    }

                    rotateRight(ancestor);

                    if(isRight){
                        node.setColor(Color.BLACK);
                        parent = null;
                    }else{
                        parent.setColor(Color.BLACK);
                    }

                    ancestor.setColor(Color.RED);
                }else{
                    boolean isLeft = node == parent.getLeft();
                    if(isLeft){
                        rotateRight(parent);
                    }

                    rotateLeft(ancestor);

                    if(isLeft){
                        node.setColor(Color.BLACK);
                        parent = null;
                    }else{
                        parent.setColor(Color.BLACK);
                    }

                    ancestor.setColor(Color.RED);
                }
            }else{
                parent.setColor(Color.BLACK);
                uncle.setColor(Color.BLACK);
                parent.getParent().setColor(Color.RED);
                node = parent.getParent();
                parent = node.getParent();
            }
        }

        getRoot().setColor(Color.BLACK);
        getRoot().setParent(null);
    }

    private void rotateRight(Node<T> node) {
        Node<T> left = node.getLeft();
        if(left == null){
            throw new IllegalStateException("");
        }

        Node<T> parent = node.getParent();
        node.setLeft(left.getRight());
        setParent(left.getRight(),node);

        left.setRight(node);
        setParent(node,left);

        if(parent == null){
            root.setLeft(left);
            setParent(left,null);
        }else{
            if(parent.getLeft() == node){
                parent.setLeft(left);
            }else{
                parent.setRight(left);
            }
            setParent(left,parent);
        }
    }

    private void rotateLeft(Node<T> node) {
        Node<T> right = node.getRight();
        if (right == null){
            throw new IllegalStateException("");
        }

        Node<T> parent = node.getParent();
        node.setRight(right.getLeft());
        setParent(right.getLeft(),node);

        right.setLeft(node);
        setParent(node,right);

        if(parent == null){
            root.setLeft(right);
            setParent(right,null);
        }else{
            if(parent.getLeft() == node){
                parent.setLeft(right);
            }else{
                parent.setRight(right);
            }
            setParent(right,parent);
        }

    }

    private Node<T> getUncle(Node<T> node) {
        Node<T> parent = node.getParent();
        Node<T> ancestor = parent.getParent();
        if (ancestor == null){
            return null;
        }

        if(parent == ancestor.getLeft()){
            return ancestor.getRight();
        }else{
            return ancestor.getLeft();
        }
    }

    private Node<T> findParentNode(Node<T> node) {
        Node<T> dataRoot = getRoot();
        Node<T> child = dataRoot;

        while (child != null){
            int cmp = child.getValue().compareTo(node.getValue());
            if(cmp == 0){
                return child;
            }
            if(cmp > 0){
                dataRoot = child;
                child = child.getLeft();
            }else if(cmp < 0){
                dataRoot = child;
                child = child.getRight();
            }
        }
        return dataRoot;
    }

    private void setParent(Node<T> node, Node<T> parent) {
        if(node != null){
            node.setParent(parent);
            if(parent == root){
                node.setParent(null);
            }
        }
    }


    public Long getSize() {
        return size.get();
    }

    public Node<T> getRoot() {
        return root.getLeft();
    }

    public boolean isOverrideMode() {
        return overrideMode;
    }

    public void setOverrideMode(boolean overrideMode) {
        this.overrideMode = overrideMode;
    }
}
class Node<T extends Comparable<T>> {
    private T value;
    private Node parent;
    private Node left;
    private Node right;
    private Color color;

    public Node(){}
    public Node(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
enum Color {
    RED,BLACK
}