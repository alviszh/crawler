 function createGuid() {
            return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
}
function ref() {
            var guid = createGuid() + createGuid() + "-" + createGuid() + "-" + createGuid() + createGuid() + "-" + createGuid() + createGuid() + createGuid(); //CreateGuid();
            return guid;
}
