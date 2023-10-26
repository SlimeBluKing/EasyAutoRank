package it.easymc.easyautorank.utils;

import net.luckperms.api.node.Node;

import java.util.HashMap;

public class NodeBuilder {
    public static Node createNode(HashMap<String, String> hashMap, String perm){
        net.luckperms.api.node.NodeBuilder<?, ?> node = Node.builder(perm);
        for (String key : hashMap.keySet()){
            node.withContext(key, hashMap.get(key));
        }
        return node.build();
    }
}
