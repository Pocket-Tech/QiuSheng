package org.pockettech.qiusheng.entity.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pockettech.qiusheng.entity.charttransfer.Download;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadResult {
    private int code;
    private List<Download> items;
    private int sid;
    private int cid;
}
