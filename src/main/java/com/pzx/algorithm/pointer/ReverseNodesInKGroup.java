package com.pzx.algorithm.pointer;

public class ReverseNodesInKGroup {

      public class ListNode {
          int val;
          ListNode next;
          ListNode() {}
          ListNode(int val) { this.val = val; }
          ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public ListNode reverseKGroup(ListNode head, int k) {
        if(head == null || k == 1)
            return head;

        ListNode fakeHeadNode = new ListNode(-1, head);
        ListNode lastGroupEndNode = fakeHeadNode;

        ListNode curNode = lastGroupEndNode.next;
        ListNode curGroupFirstNode = curNode;
        int count = 0;

        while(true){

            while(curNode != null && ++count < k){
                curNode = curNode.next;
            }
            if(count < k) break;

            reverseList(lastGroupEndNode, curNode);
            lastGroupEndNode = curGroupFirstNode;
            curNode = curGroupFirstNode.next;
            curGroupFirstNode = curNode;
            count = 0;

        }
        return fakeHeadNode.next;

    }

    private void reverseList(ListNode lastGroupEndNode, ListNode endNode){

        ListNode nextGroupFirstNode = endNode.next;
        ListNode lastNode = lastGroupEndNode.next;
        ListNode curNode = lastNode.next;

        while(curNode != nextGroupFirstNode){

            lastNode.next = curNode.next;
            curNode.next = lastGroupEndNode.next;
            lastGroupEndNode.next = curNode;

            curNode = lastNode.next;
        }
    }

}
