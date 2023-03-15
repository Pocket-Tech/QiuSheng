package org.pockettech.qiusheng.entity.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pockettech.qiusheng.entity.Download;

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
