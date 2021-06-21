package com.pzx.algorithm.pointer;

/**
 * 快慢指针
 * Floyd 判圈算法
 */
public class DetectCycle {
      class ListNode {
          int val;
          ListNode next;
          ListNode(int x) {
              val = x;
              next = null;
          }
      }


    public ListNode detectCycle(ListNode head) {

        ListNode quick = head;
        ListNode slow = head;

        while(quick != null){
            if(quick.next == null)
                return null;
            quick = quick.next.next;
            slow = slow.next;

            if(quick == slow){
                ListNode ptr = head;
                while(ptr != slow){
                    ptr = ptr.next;
                    slow = slow.next;
                }
                return ptr;
            }
        }
        return null;
    }
}
