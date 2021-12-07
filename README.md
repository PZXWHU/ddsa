算法类别：
1、动态规划（有时候需要定义多个dp数组表示不同状态下的结果，进行转换）、递归、分治算法（一般和递归或者动态规划结合）
2、双指针（有时候可以是多个指针，不一定就只是两个，但是每个指针也最多是遍历一次）
3、贪心算法
4、搜索（dfs、bfs）、回溯算法（dfs）、欧拉图
5、单调栈、单调队列（和大小顺序相关的问题）
6、并查集
7、拓扑排序
8、二分查找（当找不到解题方法时，还可以直接用二分查找来试探答案！！）
9、滑动窗口（一般使用双指针实现,滑动窗口有时并不需要一直保持窗口内的元素符合要求，比如在求最大窗口时）
10、树状数组、线段树、前缀和、差分 ：
树状数组的奇妙用法：将数组元素设置为0或1，区间和可以表示范围内符合条件的元素个数
https://leetcode-cn.com/problems/corporate-flight-bookings/solution/gong-shui-san-xie-yi-ti-shuang-jie-chai-fm1ef/
https://www.cnblogs.com/wAther/p/10600216.html
https://blog.csdn.net/yuhaomogui/article/details/98643090?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.control
11、前缀树
12、状态压缩（暴力枚举）：https://leetcode-cn.com/problems/minimum-number-of-work-sessions-to-finish-the-tasks/solution/zhuang-ya-dpshi-shi-hou-xue-xi-yi-xia-li-q4mk/
13、数学分析
14、博弈（一般使用动态规划/递归解决）：
https://leetcode-cn.com/problems/predict-the-winner/
https://leetcode-cn.com/problems/guess-number-higher-or-lower-ii/
https://leetcode-cn.com/problems/stone-game
15、多线程控制顺序：
（1）Lock + Condition + Flag
（2）Flag + 空转/Thread.yield()
（3）CountDownLatch / Semaphore


## 栈的使用

1. 一般栈的使用场景，**已访问的元素还需要后续使用，所以需要栈暂存起来**，
等候之后满足条件时的再次访问。这种一般用于全部元素入栈完之后，再一个一个出栈处理。
比如利用栈记录访问路径，然后再对访问路径上的元素进行回溯处理。但有时也需要中途出栈某个元素，
比如树的非递归遍历。
2. 单调栈(经常适用于数据问题且保持一定大小顺序)：栈中的元素一般是**需要满足一定规律，比如递增，递减**，
当入栈元素不满足规律时，要进行出栈操作，并进行处理。这种一般用于访问过程中一直入栈，
当遇到不符要求的元素，则执行出栈操作。然后再继续进行入栈操作。
3. 有时候栈需要添加一对元素，但是java没有好用的tuple类，所以可以分开两个元素连续入栈和连续出栈，
从而避免了使用tuple类。


## 二维平面（矩阵）的问题！！！！

1.二维平面大多数可从列或者行的角度看过去（将整行或者整列相加），然后转换为一维问题
2.一般都可以用动态规划法解决

## 双指针或者多指针
1. 多个指针通常在一个循环中只有一个指针移动，即满足条件的特定指针（指向的值大小比较）
2. 当指针移动后，条件会发生改变，根据情况转换移动的指针。重复操作，直到指针相遇或者满足一定情况。
3. 通常用于数组首尾指针向内逼近
4.对于同一方向移动的双指针，一般是双动。一般会有一个指针的步长较大（快慢指针，**Floyd 判圈算法**），或者一开始领先N步（一般和链表距离相关）

## 树的递归
1. 树递归方法要返回处理后子树的根节点
2. 一般递归方法要返回处理的暂时结果
3. 有时递归方法需要上一步递归所处理的节点信息，但是函数返回值已经确定，可以用类内部成员代替
4. 写递归一定注意递归函数中的每一条语句都会在每一次递归中执行。一些最后汇总写的代码应该写在递归函数以外
。所以递归一般需要写两个函数，一个专门用来递归，一个用来开始以及最后的处理。

## 递归转循环
1. 一般在一个函数中递归调用自己两次，可以使用栈来转换成循环,栈记录当前状态以备后续使用（树的前中后序遍历）
2. 如果只调用自己一次，则可以直接转换为循环（尾递归）
3. 有些时候可以把递归反过来，正向推导，即**动态规划方法**
4. 递归终止条件不满足和栈非空一般作为循环的条件

总结
- 当函数中多次递归操作之后，最后还有合并操作，一般可以使用动态规划写成循环。
- 如果函数中有多次递归操作，而其他操作在递归操作之前或者之中，则可以使用栈记录状态，写成循环
- 如何函数中只有一次递归调用，可以直接进行变量的循环赋值改变条件，从而循环操作

## dp问题
- 一般二维情况下A[i][j] = f(A[i-1][j],A[i][j-1],A[i-1][j-1])
- 要考虑从A[i-1][j],A[i][j-1],A[i-1][j-1]转移到A[i][j]是什么样的方式：配合 or 独立
    - 配合就需要对A[i-1][j],A[i][j-1],A[i-1][j-1]做加减乘除等运算的得到A[i][j]
    - 独立就表示A[i-1][j],A[i][j-1],A[i-1][j-1]都可以独立的转化为A[i][j]，所以一般挑选最优的方式转换（比如min或者max）
在进行dp正向推导时，一般为i、j的递增的双重循环，即求解顺序为[0][0]、[0][1]...[1][0]、[1][1]...

- 二维情况下A[i][j]还有可能最后所求结果是A[0][m]。这种递推公式一般为A[i][j] = f(A[i+1][j],A[i][j-1],A[i+1][j-1])
在进行dp正向推导时，首先计算i、j差值为0，1，2...，最后得到i、j差值为m的结果，
即求解顺序为[0][0],[1][1]...[0][1],[1][2]...[0][2],[1][3]...[0][m]

- 有时候，动态规划并不能直接求得问题的结果，即结果为数组中的一个值。有时，需要对数组中值的意义进行
限制，方便递推。最后问题结果为数组中的最大或者最小值，或者一些特殊值。

- 二维dp的递推式当A[i][..] = A[i - 1][..] + A[i - 1][..] + ...，即i的状态只与i-1状态相关，可以将二维dp改写为一维dp
将二维数组看成多个一维数组，下一行的数组状态至于上一行相关，那么将下一行数组直接写入上一行数组的位置，则不再需要创建二维数组
但是在写入下一行数组时，不能覆盖掉还需要在后续使用的上一行数组值，则写入时可以从数组右向左写（按照实际情况判断）

- **dp问题有可能在某一个位置具有多种状态，所以可能需要多个数组，每个数组在相同位置考虑不同的状态**
状态转移时，下一位置需要根据不同情况从不同数组的上一位置推导。


## 回溯算法
- 如果解决一个问题有多个步骤，每一个步骤有多种方法，题目又要我们找出所有的方法，可以使用回溯算法；
- 回溯算法是在一棵树上的 深度优先遍历（因为要找所有的解，所以需要遍历）；
- 回溯算法一般使用递归实现，并且将递归状态作为参数传入递归函数中，在每一步中进行改变。
回溯算法在一次递归结束后，需要消除此次递归所带来的状态影响，从而正确的进行下一次向下尝试。
- 回溯 = 深度优先遍历 + 状态重置 + 剪枝。 https://blog.csdn.net/lw_power/article/details/103795299
- 回溯算法的时间复杂度可以根据搜索状态空间进行判断，一般如果递归函数f(n)中调用了n次f(n-1)函数，则时间复杂度为n！；
如果递归函数f(n)中依次调用了f(n-1)、f(n-2)...f(2)、f(1)，则时间复杂度为2^n。

## 字符串处理
- 如果要对遍历过的字母进行contains判断，一般要使用哈希表储存访问过的字母，一般value会存放字母对应的位置
并且在后续遍历之中进行更新。或者使用HashSet，删除不再使用的字母。

## 层序遍历
有时处理层序遍历时，需要将每一层分开处理。一般有两种方法：
1. 每次判断队列中的元素数量size，进行size大小的for循环，将某一层的元素一次性处理完。然后队列中剩下的元素即为下一层元素
2. 将某层节点全部加载入队列中后，加入标志节点，代表一层节点以及处理完毕

## 整数溢出问题
在处理整数时，一定要注意整数溢出问题。
负数转整数可能出现溢出！！！整数相加也可能出现溢出！！

## 前缀和和后缀和
1. 常用于需要计算数组中大部分数据的和、乘积之类，用于累计计算
（一次遍历即可计算出数据中所有元素的前缀和，不用反复循环计算一些数的和或者乘积），减少时间复杂度
2. 利用前缀和或者后缀和可以快速计算出任意连续子数组的和、积
3. 可以利用单个变量记录前缀和，即算即用，减少空间复杂度
4. 不光数组有前缀和，树在遍历过程中也可以有前缀和

## log(n)时间复杂度
一看到log(n)一般代表二分法，进行扩展就是N分法，每次以常数时间把问题规则缩小到1/N。


## 括号处理
利用计数，左括号+1， 右括号-1，判断字符串性质
一个给定括号字符串，如果想要变正常，左右括号可能都需要

## 复杂问题转化为简单问题
有些问题正向思考特别复杂，需要将其转换为一个简单的问题。利用逆向思维或者转化思维对其进行变换。

## 根据数据范围推导时间复杂度
https://blog.csdn.net/qq_41775852/article/details/113757367

## 单调栈 and 单调队列
一般用于和大小顺序有关的情景
栈和队列中的元素保证单调性

## 常数时间返回最值
一般使用相似的数据结构记录每一位置的最值，通常与单调栈 and 单调队列结合使用    
LC155. 最小栈  LC239. 滑动窗口最大值

## 数组小技巧
1. 可以使用数组元素的序号和数组值进行对应。比如将相应值放入对应的位置，以便检测缺失的值。
2. 将值和序号对应构建边，从而生成图

## 整数之和 = target
1、对于在于同一个数组中的，可以先排序，然后枚举 + 双指针
2、对于在不同数组中的，可以分组 + 枚举 + 哈希表 

## 树的根节点
入度为0

## 区间求和
此处可以再总结一下（加粗字体为最佳方案）：
数组不变，区间查询：**前缀和**、树状数组、线段树；
数组单点修改，区间查询：**树状数组**、线段树；
数组区间修改，单点查询：**差分**、线段树；
数组区间修改，区间查询：**线段树**。

## 回文串
动态规划  双指针  KMP  最大相同前后缀  拼接  字符串hash法  中心扩展法

## 连续元素和为0
可计算前缀和，并使用哈希表进行记录，当出现两个相同的值时，其对应的索引下标之间的元素和就为0
https://leetcode-cn.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/

## 连续子数组问题
1. 滑动窗口
2. 前缀和

## BFS求最短距离
1. 从单节点向外扩散，找最短距离
2. 多节点向内扩散，找最短距离

## 字典序最小的子序列
一般使用单调栈或者单调队列解决

## 后序遍历
后序遍历的逆序类似于前序遍历（父节点，右节点，左节点）

## 持续生成范围减小的随机数
1.基于数组 2.基于哈希表
https://leetcode-cn.com/problems/random-flip-matrix/solution/sui-ji-fan-zhuan-ju-zhen-by-leetcode-sol-pfmr/

## 欧拉路径和欧拉回路
欧拉图或者半欧拉图有定义：
1.通过图中所有边恰好一次且行遍所有顶点的通路称为欧拉通路。
2.通过图中所有边恰好一次且行遍所有顶点的回路称为欧拉回路。
3.具有欧拉回路的无向图称为欧拉图。
4.具有欧拉通路但不具有欧拉回路的无向图称为半欧拉图。

如果没有保证至少存在一种合理的路径，我们需要判别这张图是否是欧拉图或者半欧拉图，具体地：
1.对于无向图 GG，GG 是欧拉图当且仅当 GG 是连通的且没有奇度顶点。
2.对于无向图 GG，GG 是半欧拉图当且仅当 GG 是连通的且 GG 中恰有 22 个奇度顶点。
3.对于有向图 GG，GG 是欧拉图当且仅当 GG 的所有顶点属于同一个强连通分量且每个顶点的入度和出度相同。
3.对于有向图 GG，GG 是半欧拉图当且仅当 GG 的所有顶点属于同一个强连通分量且
恰有一个顶点的出度与入度差为 1；
恰有一个顶点的入度与出度差为 1；
所有其他顶点的入度和出度相同。

Hierholzer 算法：
1.从起点出发，进行深度优先搜索。
2.每次沿着某条边从某个顶点移动到另外一个顶点的时候，都需要删除这条边。
3.如果没有可移动的路径，则将所在节点加入到栈中，并返回。

