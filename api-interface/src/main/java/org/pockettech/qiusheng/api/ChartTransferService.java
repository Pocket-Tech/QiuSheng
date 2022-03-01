package org.pockettech.qiusheng.api;

import org.pockettech.qiusheng.entity.Result.DownloadResult;
import org.pockettech.qiusheng.entity.charttransfer.ChartSign;

public interface ChartTransferService {
    ChartSign uploadSign(Integer sid, Integer cid, String name, String hash);
    DownloadResult chartDownload(int cid);
}
