package com.dx168.fastdex.build.lib.snapshoot.string
/**
 * Created by tong on 17/3/31.
 */
public final class StringSnapshoot extends BaseStringSnapshoot<StringDiffInfo,StringNode> {
    public StringSnapshoot() {
    }

    public StringSnapshoot(StringSnapshoot snapshoot) {
        super(snapshoot);
    }

    public StringSnapshoot(Set<String> strings) throws IOException {
        super(strings);
    }

    public StringSnapshoot(String... strings) throws IOException {
        super(strings);
    }
}
