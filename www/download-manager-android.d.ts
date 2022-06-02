export as namespace AndroidDownloadManager;
export = AndroidDownloadManager;

declare class AndroidDownloadManager {
    enqueue: AndroidDownloadManager.EnqueueFunction;
    query: AndroidDownloadManager.QueryFunction;
    remove: AndroidDownloadManager.RemoveFunciton;
}

declare namespace AndroidDownloadManager {
    type EnqueueFunction = (data: EnqueueData, success: SuccessEnqueueCallback, error: ErrorCallback) => void;
    type QueryFunction = (data: QueryData, success: SuccessQueryCallback, error: ErrorCallback) => void;
    type RemoveFunciton = (data: RemoveData, success: SuccessRemoveCallback, error: ErrorCallback) => void;

    /** GENERAL PART */
    type ErrorCallback = (error: string) => void;

    /** ENQUEUE PART */
    interface EnqueueData {
        url: string;
        fileName?: string;
        title?: string;
        description?: string;
        mimeType?: string;
        header?: [
            {
                header: string;
                value: string;
            }
        ]
    }
    type SuccessEnqueueCallback = (id: string) => void;

    /** QUERY PART */
    interface QueryData {
        id: string;
    }
    type SuccessQueryCallback = (data: DownloadStatus) => void;
    interface DownloadStatus {
        id: string;
        title: string;
        description: string;
        mimeType: string;
        localUri: string;
        mediaProviderUri: string;
        uri: string;
        lastModifiedTimestamp: string;
        status: string;
        reason: string;
        bytesDownloadedSoFar: string;
        totalSizeBytes: string;
    }

    /** REMOVE PART */
    interface RemoveData {
        id: string;
    }
    type SuccessRemoveCallback = (info: 'done' | 'failed') => void;
}