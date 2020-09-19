学习笔记
|  题号   | 名称  | 类型 |备注|
|  ----  | ----  | ---- |----|
|  [242](https://leetcode-cn.com/problems/valid-anagram/)  | [有效的字母异位词](https://leetcode-cn.com/problems/valid-anagram/) ||解法1：hash表的解法时间复杂度是O(n), 空间复杂度是O(1) 解法2：排序后数组进行比较，时间复杂度是O(n log n), 空间复杂度是O(1)|

## 二叉树遍历
+ 前序遍历，根-左-右
  ```
  private void preorder(TreeNode root, List<Integer> list) {
      if(root == null) {
          return;
      }
      list.add(root.val);
      preorder(root.left, list);
      preorder(root.right, list);
  }
  ```
+ 中序遍历，左-根-右
  ```
  private void inorder(TreeNode root, List<Integer> list) {
      if(root == null) {
          return;
      }
      inorder(root.left, list);
      list.add(root.val);
      inorder(root.right, list);
  }
  ```
+ 后续遍历，左-右-根
  ```
  private void postorder(TreeNode root, List<Integer> list) {
      if(root == null) {
          return;
      }
      postorder(root.left, list);
      postorder(root.right, list);
      list.add(root.val);
  }
  ```

## N叉树遍历
+ 前序遍历，遵循二叉树的前序遍历原则。
  ```
  private void preorder(Node root, List<Integer> list) {
    if(root == null) return;
    list.add(root.val);
    for(int i = 0; i < root.children.size(); i++) {
      preorder(root.children.get(i), list);
    }
  }
  ```
+ 中序遍历，遵循二叉树的中序遍历原则。
+ 后序遍历，遵循二叉树的后序遍历原则。

## 堆
Heap: 可以迅速找到一堆数中的最大值或者最小值的数据结构。
大顶堆(大根堆): 根节点最大的堆
小顶堆(小根堆): 根节点最小的堆

### 二叉堆
#### 性质
1. 是一颗完全树
2. 树中任意节点的值总是 >= 其子节点的值

#### 实现细节
1. 二叉堆一般通过 数组 实现
2. 假设第一个元素在数组中的索引为0的话，其父节点和子节点的位置关系如下:
(1) 索引为i的左孩子的索引是(2*i + 1)
(2) 索引为i的右孩子的索引是(2*i + 2)
(3) 索引为i的父节点的索引是((i-1) / 2)

#### insert 插入操作
1. 新元素一律先插入到堆的尾部
2. 依次向上调整整个堆的结构，一直到根，HeapifyUp

#### delete max 删除堆顶操作
1. 将堆尾元素替换到顶部(即对顶被替代删除掉)
2. 依次从根部向下调整整个堆的结构(一直到堆尾即可)，HeapifyDown