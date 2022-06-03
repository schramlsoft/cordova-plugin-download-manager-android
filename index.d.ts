export declare interface DownloadManagerAndroidInterface {
    enqueue: EnqueueFunction;
    query: QueryFunction;
    remove: RemoveFunciton;
}

export declare type EnqueueFunction = (data: EnqueueData, success: SuccessEnqueueCallback, error: ErrorCallback) => void;
export declare type QueryFunction = (data: QueryData, success: SuccessQueryCallback, error: ErrorCallback) => void;
export declare type RemoveFunciton = (data: RemoveData, success: SuccessRemoveCallback, error: ErrorCallback) => void;

/** GENERAL PART */
export declare type ErrorCallback = (error: string) => void;

/** ENQUEUE PART */
export declare interface EnqueueData {
    url: string;
    fileName?: string;
    title?: string;
    description?: string;
    mimeType?: string;
    header?: EnqueueHeaderData[]
}
export declare interface EnqueueHeaderData {
    header: string;
    value: string;
}

export declare type SuccessEnqueueCallback = (id: string) => void;

/** QUERY PART */
export declare interface QueryData {
    id: string;
}
export declare type SuccessQueryCallback = (data: DownloadStatus) => void;
export declare interface DownloadStatus {
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
export declare interface RemoveData {
    id: string;
}
export declare type SuccessRemoveCallback = (info: 'done' | 'failed') => void;