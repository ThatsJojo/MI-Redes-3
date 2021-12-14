export const convertArrayToObject = (array, callBackItemKey, callBackItem = null) => {
    const initialValue = {};
    return array.reduce((obj, item, key) => {
        return {
            ...obj,
            [callBackItemKey(item, key)]: callBackItem == null ? item : callBackItem(item),
        };
    }, initialValue);
};