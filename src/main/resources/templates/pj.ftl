<!DOCTYPE html>
<html lang="en" style="width:768px;">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>我的评价</title>
</head>
<body  style="background: #f4f4f4;color: #666;font-size: .12rem;line-height: 1.5;margin: auto;max-width: 768px;font-family: PingFangSC-Regular,Helvetica,STHeiti STXihei,Microsoft YaHei,Microsoft JhengHei,Arial;">
<div id="main">
    <div class="comments-content">
        <div>
            <div>
                <div class="infinite-list-wrapper Baselist-wrapper" role="list" id="my-comments-list">
                    <div class="my-comment-item" style="padding: 0px 25px 25px 25px;background: #fff; margin-bottom: 16px;">
                        <div class="comment-customer" style="height: 115px;display:flex;box-align: center;align-items: center;align-items: center;box-orient: horizontal;box-direction: normal;flex-direction: row; flex-direction: row;">
                            <div class="comment-customer-avatar" style="width: 73px;height: 73px;margin-right: 16px;display: inline-block;">
                                <img src="${userImg}" style="border-radius:50px;width:100%;height:100%;">
                            </div>
                            <div class="comment-customer-desc" style="display: inline-block;width:400px;">
                                <div class="comment-customer-name" style="font-size:30px;color: #151516;margin-top: 4px;margin-bottom: 12px;overflow: hidden;">177****7870</div>
                                <div class="comment-customer-time" style="font-size:25px;color: #9c9c9c;">${pjTime}</div>
                            </div>
                            <div class="comment-customer-operator"></div>
                        </div>
                        <div class="comment-score-box" style="background: #f5f5f5;border-radius: .04rem;width: 690px;height: 325px;padding: 14px;margin-bottom: 25px;">
                            <div class="goods-desc-box" style="padding-bottom: 20px;height: 160px;border-bottom: 1px solid #ededed;">
                                <div class="goods-img-wrapper" style="width: 150px;height: 150px;background-color: #fafafa;background-size: contain;overflow: hidden;vertical-align: top;display: inline-block;">
                                    <img class="goods-img" style="border: 0;max-width: 100%;width: 150px;height: 150px;vertical-align: top;" src="${goodsImg}">
                                </div>
                                <div class="goods-desc" style="width: 512px;display: inline-block;margin-left: 20px;">
                                    <div class="goods-name" style="width: 512px;height: 38px;font-size: 27px;color: #151516;line-height: 38px;overflow: hidden;<#if goodsName02 ??><#else>margin: 0px 0px 33px;</#if>">${goodsName01}</div>
                                    <#if goodsName02 ??>
                                    <div class="goods-name" style="width: 512px;height: 38px;font-size: 27px;color: #151516;line-height: 38px;margin: 0px 0px 33px 0px;overflow: hidden;">${goodsName02}</div>
                                    </#if>
                                    <div class="price-box" style="overflow: hidden;text-overflow: ellipsis;line-clamp: 2;box-orient: vertical;line-height: 30px;height: 30px;">
                                        <span class="price" style="color: #151516;font-size: 26px;">￥${orderAmount}</span><span class="sku" style="color: #9c9c9c;margin-left: 25px;font-size: 26px;"><span>${spec01}</span></span>
                                    </div>
                                    <#if spec02 ??>
                                    <div class="price-box" style="overflow: hidden;text-overflow: ellipsis;line-clamp: 2;box-orient: vertical;line-height: 30px;height: 30px;">
                                        <span class="sku" style="color: #9c9c9c;font-size: 26px;"><span>${spec02}</span></span>
                                    </div>
                                    </#if>
                                    
                                </div>
                            </div>
                            <div class="score" style="width: 100%;height: 120px;padding-top: 10px;">
                                <img src="http://yhj6688.top:8799/dftt-service/pdd/image/star.png" style="width:350px;height:120px;">
                            </div>
                        </div>
                        <div class="comment-text-box">
                             <div class="comment-text" style="line-height: 45px;font-size: 30px;color: #151516;height: 45px;overflow: hidden;">${comment01}</div>
                             <#if comment02 ??>
                             <div class="comment-text" style="line-height: 45px;font-size: 30px;color: #151516;height: 45px;overflow: hidden;">${comment02}</div>
                             </#if>
                             <#if comment03 ??>
                             	<div class="comment-text" style="line-height: 45px;font-size: 30px;color: #151516;height: 45px;overflow: hidden;">${comment03}</div>
                             </#if>
                             
                         </div>
                         <div class="comment-imgs" style="margin-top: 14px;display: flex;box-pack: start;justify-content: flex-start;box-align: center;align-items: center;box-orient: horizontal;box-direction: normal;flex-direction: row;width: 100%;overflow-x: scroll;overflow-y: hidden;">
                             <#list pjImgs! as pjImage> 
							    <div style="width: 230px;height: 230px;flex-shrink: 0;margin-right: 6px;display: inline-block;" >
	                                 <img style="width: 230px;height: 230px;" src="${pjImage}">
	                             </div>
							</#list>
                         </div>
                         <div class="bottom-box" style="">
                             <img src="http://yhj6688.top:8799/dftt-service/pdd/image/btn.png" style="width:718px;height:128px;">
                         </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>