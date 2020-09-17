Java 中的HashMap

## HashMap的数据结构
数组+链表+红黑树，当链表长度超过8时且数组长度大于64，将链表转成红黑树
## put()实现原理
1. 调用hash方法计算key的hash值，然后结合数组的长度计算出下标
   ```
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    下标 = (数组的长度 - 1) & hash
   ```
2. 调整数组大小，也就是扩容，如果大于等于MAXIMUM_CAPACITY(2^30)则直接等于Integer.MAX_VALUE，扩容成当前容量的两倍
   ```
    if (oldCap > 0) {
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
            oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
        }
   ```
3. 如果key计算出来的索引对应的值为空，则直接插入；
   ```
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
   ```
   如果不为空，也就是发生了hash碰撞，如果equals为true则更新键值对
   ```
    if (p.hash == hash &&((k = p.key) == key || (key != null && key.equals(k))))
        e = p;

    if (e != null) { // existing mapping for key
        V oldValue = e.value;
        if (!onlyIfAbsent || oldValue == null)
        e.value = value;
        afterNodeAccess(e);
        return oldValue;
    }
   ```
   如果equals为false，则判断是否为树节点
   ```
    else if (p instanceof TreeNode)
        e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
   ```
   如果不是树节点，则遍历链表，如果binCount >= TREEIFY_THRESHOLD - 1 且 数组长度大于 64 则转换成红黑树
   ```
    else {
        for (int binCount = 0; ; ++binCount) {
            if ((e = p.next) == null) {
                p.next = newNode(hash, key, value, null);
                if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                    treeifyBin(tab, hash);
                    break;
            }
            if (e.hash == hash &&((k = e.key) == key || (key != null && key.equals(k))))
                break;
            p = e;
        }
    }
   ```
   ```
       final void treeifyBin(Node<K,V>[] tab, int hash) {
        int n, index; Node<K,V> e;
        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
            resize();
        else if ((e = tab[index = (n - 1) & hash]) != null) {
            TreeNode<K,V> hd = null, tl = null;
            do {
                TreeNode<K,V> p = replacementTreeNode(e, null);
                if (tl == null)
                    hd = p;
                else {
                    p.prev = tl;
                    tl.next = p;
                }
                tl = p;
            } while ((e = e.next) != null);
            if ((tab[index] = hd) != null)
                hd.treeify(tab);
        }
    }
   ```
## resize()扩容原理