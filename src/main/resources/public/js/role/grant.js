var zTreeObj;
$(function () {
    loadModuleInfo();
});
function loadModuleInfo() {
    $.ajax({
        type:"post",
        url:ctx+"/module/queryAllModules",
        data:{
            roleId:$("#roleId").val()
        },
        dataType:"json",
        success:function (data) {
            // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
            var setting = {
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                view:{
                    showLine: false
                // showIcon: false
                },
                check: {
                    enable: true,
                    chkboxType: { "Y": "ps", "N": "ps" }
                },
                callback: {
                    onCheck: zTreeOnCheck
                }
            };
            var zNodes =data;
            zTreeObj=$.fn.zTree.init($("#test1"), setting, zNodes);
        }
    })
}

function zTreeOnCheck(event, treeId, treeNode) {
    //所选中的所有资源  id name pId
    var nodes= zTreeObj.getCheckedNodes(true);
    //隐藏域的角色id
    var roleId=$("#roleId").val();
    //收集数据
    var mids="mids=";
    for(var i=0;i<nodes.length;i++){
        //判断是否到最后一个元素
        if(i<nodes.length-1){
            mids=mids+nodes[i].id+"&mids=";
        }else{
            mids=mids+nodes[i].id;
        }
    }
    $.ajax({
        type:"post",
        url:ctx+"/role/addGrant",
        data:mids+"&roleId="+roleId,
        dataType:"json",
        success:function (data) {
            console.log(data.msg);
        }
    })
}