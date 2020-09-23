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
1. 检查数组长度是否大于0，
   (1) 检查是否超过最大的容量，如果超过则将阈值设置为Integer.MAX_VALUE，并直接返回原来的数组
   (2) 如果没超过最大容量，则扩容为原来的两倍
2. 检查阈值是否大于0,如果大于0，则新的容量等于这个阈值
3. 初始化默认的容量 DEFAULT_INITIAL_CAPACITY(16)，初始化默认的阈值(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY)
```
if (oldCap > 0) {
    if (oldCap >= MAXIMUM_CAPACITY) {
        threshold = Integer.MAX_VALUE;
        return oldTab;
    }
    else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY)
        newThr = oldThr << 1; // double threshold
}
else if (oldThr > 0) // initial capacity was placed in threshold
    newCap = oldThr;
else {               // zero initial threshold signifies using defaults
    newCap = DEFAULT_INITIAL_CAPACITY;
    newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
}
```
4. 如果旧数组不为null，则将旧数据转移到新数组中
   (1) 如果不是链表(if (e.next == null))，则直接计算出索引值添加到新数组
   (2) 检查是不是TreeNode
   (3) 遍历链表，如果(e.hash & oldCap) == 0，则索引的位置不变，否则会重新计算新的索引，新的索引是 j(当前索引) + oldCap(旧容量)
```
    if (oldTab != null) {
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // preserve order
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
```
## hash方法的实现
1. 如果key为null，则默认的索引是0
```
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```