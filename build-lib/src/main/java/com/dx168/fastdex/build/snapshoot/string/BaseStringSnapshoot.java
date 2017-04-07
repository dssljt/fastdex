package com.dx168.fastdex.build.snapshoot.string;

import com.dx168.fastdex.build.snapshoot.api.DiffInfo;
import com.dx168.fastdex.build.snapshoot.api.DiffResultSet;
import com.dx168.fastdex.build.snapshoot.api.Snapshoot;
import com.dx168.fastdex.build.snapshoot.api.Status;

import java.io.IOException;
import java.util.Set;

/**
 * Created by tong on 17/3/31.
 */
public class BaseStringSnapshoot<DIFF_INFO extends StringDiffInfo,NODE extends StringNode> extends Snapshoot<DIFF_INFO,NODE> {

    public BaseStringSnapshoot() {
    }

    public BaseStringSnapshoot(BaseStringSnapshoot snapshoot) {
        super(snapshoot);
    }

    public BaseStringSnapshoot(Set<String> strings) throws IOException {
        for (String str : strings) {
            addNode((NODE) StringNode.create(str));
        }
    }

    public BaseStringSnapshoot(String ...strings) throws IOException {
        for (String str : strings) {
            addNode((NODE) StringNode.create(str));
        }
    }

    @Override
    protected DiffInfo createEmptyDiffInfo() {
        return new StringDiffInfo();
    }

    @Override
    protected void diffNode(DiffResultSet<DIFF_INFO> diffInfos, Snapshoot<DIFF_INFO, NODE> otherSnapshoot, NODE now, NODE old) {
        //不需要对比变化
        addDiffInfo(diffInfos,createDiffInfo(Status.NOCHANGED,now,old));
    }
}
