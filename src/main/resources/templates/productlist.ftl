<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>搜索商品</title>
    <style>
        html {background: #f4f4f4;font-family: PingFangSC-Regular, Helvetica, STHeiti STXihei, Microsoft YaHei, Microsoft JhengHei, Arial;}
        body {margin: auto;width: 505px;height: 866px;padding: 0px;font-family:PingFangSC-Regular, Helvetica, STHeiti STXihei, Microsoft YaHei, Microsoft JhengHei, Arial;}
    </style>
</head>

<body>
<div style="width:505px;height:866px;margin: 0px;padding:0px;">
    <div style="width:505px;position: relative;background-color: #fafafa;">
        <div style="width:505px;height:141px;">
            <img style="background-color:#fff;width:505px;height:141px;" src="http://yhj6688.top:8799/dftt-service/pdd/image/search_top.png">
        </div>
        <div style="position:absolute;top:45px;left:125px;font-weight: 500;font-size:23px;width:310px;height:30px;overflow: hidden;">${keyword}</div>
    	<div style="position:absolute;top:3px;left:10px;font-weight: 500;font-size:16px;width:80px;height:30px;overflow: hidden;">${time}</div>
    </div>
    <div style="float:left;width:250px;height:725px;margin: 0 5px 0 0;">
        <div style="width:250px;height:360px;background-color: #fff;margin-bottom: 5px;">
            <div style="width:250px;height:250px;position: relative;background-color: #fafafa;">
                <div style="width:250px;height:250px;">
                    <img style="background-color:#fff;width:250px;height:250px;" src="${product1.goodsImg}">
                </div>
                <#if product1.mallName ?? &&  product1.mallStyle=="1">
                	<div style="background-color:#eaca93;color: #572600!important;position:absolute;bottom:0;left:12px;padding:0 5px;border-bottom-left-radius:0;border-bottom-right-radius:0;max-width:218px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
	                    ${product1.mallName}
	                </div>
                </#if>
                <#if product1.mallName ?? &&  product1.mallStyle=="0">
                	<div style="background:#FFF;color:#666;position:absolute;bottom:0;left:12px;padding:0 5px;border-bottom-left-radius:0;border-bottom-right-radius:0;max-width:218px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
	                    ${product1.mallName}
	                </div>
                </#if>
            </div>
            <div style="margin:10px;">
                <div style="width:225px;height:25px;line-height:25px;margin-bottom:4px;font-size:16px;color:#151516;word-wrap:normal;word-break:keep-all;overflow: hidden;">
                    <#if product1.prefixIcons01 ??>
                         <img style="float: left;height:22px;line-height:22px;padding:2px 0px;top:0px;margin-right: 6px;" src="${product1.prefixIcons01}">
                    </#if>
                    <#if product1.prefixIcons02 ??>
                         <img style="float: left;height:22px;line-height:22px;padding:2px 0px;top:0px;margin-right: 6px;" src="${product1.prefixIcons02}">
                    </#if>
                    <#if product1.prefixIcons03 ??>
                         <img style="float: left;height:22px;line-height:22px;padding:2px 0px;top:0px;margin-right: 6px;" src="${product1.prefixIcons03}">
                    </#if>
                    <div style="float: left;position:relative;overflow: hidden;">
                         <div style="width:${product1.goodsNameWith};position:absolute;top:0px;left:0px;right:10px;height:25px;line-height:25px;overflow: hidden;">${product1.goodsName}</div>
                    </div>
                </div>
                <div style="width:225px;height:20px;margin:5px 0px;white-space: nowrap;overflow:hidden;">
                	<#if product1.tag01 ??>
                		<div style="background: ${product1.tagBackgroudColor01}; color: ${product1.tagColor01};display: inline-block;height:20px;line-height:20px;margin: 0px 5px 2px 0px;font-size: 16px;padding:0 3px;">
                		<#if product1.tagurl01 ??>
                		<img style="float: left;height:20px;margin-right: 0px;" src="${product1.tagurl01}" />
                		</#if>
                		${product1.tag01}
                		</div>
                	</#if>
                   <#if product1.tag02 ??>
                		<div style="background:${product1.tagBackgroudColor02}; color: ${product1.tagColor02};display: inline-block;height:20px;line-height:20px;margin: 0px 5px 2px 0px;font-size: 16px;padding:0 3px;">
                		<#if product1.tagurl02 ??>
                		<img style="float: left;height:20px;margin-right: 0px;" src="${product1.tagurl02}" />
                		</#if>
                		${product1.tag02}
                		</div>
                	</#if>
                	<#if product1.tag03 ??>
                		<div style="background:${product1.tagBackgroudColor03}; color: ${product1.tagColor03};display: inline-block;height:20px;line-height:20px;margin: 0px 5px 2px 0px;font-size: 16px;padding:0 3px;">
                		<#if product1.tagurl03 ??>
                		<img style="float: left;height:20px;margin-right: 0px;" src="${product1.tagurl03}" />
                		</#if>
                		${product1.tag03}
                		</div>
                	</#if>
                </div>
                <div style="width:225px;height:35px;margin-top:5px;">
                    <div style="font-size:25px;max-width:190px;white-space:nowrap;white-space: nowrap;overflow:hidden;color:#e02e24;font-weight:700;">
                        <span style="display: inline-block;color:#e02e24;font-size: 18px;margin-right: 1px;">¥</span>${product1.price}
                        <span style="display: inline-block;color:#9c9c9c;font-size: 16px;font-weight:400;">${product1.salesTip}</span>
                    </div>
                </div>
            </div>
        </div>
        <div style="width:250px;height:360px;background-color: #fff;margin-bottom: 5px;">
            <div style="width:250px;height:250px;position: relative;background-color: #fafafa;">
                <div style="width:250px;height:250px;">
                    <img style="background-color:#fff;width:250px;height:250px;" src="${product3.goodsImg}">
                </div>
                <#if product3.mallName ?? &&  product3.mallStyle=="1">
                	<div style="background-color:#eaca93;color: #572600!important;position:absolute;bottom:0;left:12px;padding:0 5px;border-bottom-left-radius:0;border-bottom-right-radius:0;max-width:218px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
	                    ${product3.mallName}
	                </div>
                </#if>
                <#if product3.mallName ?? &&  product3.mallStyle=="0">
                	<div style="background:#FFF;color:#666;position:absolute;bottom:0;left:12px;padding:0 5px;border-bottom-left-radius:0;border-bottom-right-radius:0;max-width:218px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
	                    ${product3.mallName}
	                </div>
                </#if>
            </div>
            <div style="margin:10px;">
                <div style="width:225px;height:25px;line-height:25px;margin-bottom:4px;font-size:16px;color:#151516;word-wrap:normal;word-break:keep-all;overflow: hidden;">
                    <#if product3.prefixIcons01 ??>
                         <img style="float: left;height:22px;line-height:22px;padding:2px 0px;top:0px;margin-right: 6px;" src="${product3.prefixIcons01}">
                    </#if>
                    <#if product3.prefixIcons02 ??>
                         <img style="float: left;height:22px;line-height:22px;padding:2px 0px;top:0px;margin-right: 6px;" src="${product3.prefixIcons02}">
                    </#if>
                    <#if product3.prefixIcons03 ??>
                         <img style="float: left;height:22px;line-height:22px;padding:2px 0px;top:0px;margin-right: 6px;" src="${product3.prefixIcons03}">
                    </#if>
                    <div style="float: left;position:relative;overflow: hidden;">
                         <div style="width:${product3.goodsNameWith};position:absolute;top:0px;left:0px;right:10px;height:25px;line-height:25px;overflow: hidden;">${product3.goodsName}</div>
                    </div>
                </div>
                <div style="width:225px;height:20px;margin:5px 0px;white-space: nowrap;overflow:hidden;">
                	<#if product3.tag01 ??>
                		<div style="background: ${product3.tagBackgroudColor01}; color: ${product3.tagColor01};display: inline-block;height:20px;line-height:20px;margin: 0px 5px 2px 0px;font-size: 16px;padding:0 3px;">
                		<#if product3.tagurl01 ??>
                		<img style="float: left;height:20px;margin-right: 0px;" src="${product3.tagurl01}" />
                		</#if>
                		${product3.tag01}
                		</div>
                	</#if>
                   <#if product3.tag02 ??>
                		<div style="background:${product3.tagBackgroudColor02}; color: ${product3.tagColor02};display: inline-block;height:20px;line-height:20px;margin: 0px 5px 2px 0px;font-size: 16px;padding:0 3px;">
                		<#if product3.tagurl02 ??>
                		<img style="float: left;height:20px;margin-right: 0px;" src="${product3.tagurl02}" />
                		</#if>
                		${product3.tag02}
                		</div>
                	</#if>
                	<#if product3.tag03 ??>
                		<div style="background:${product3.tagBackgroudColor03}; color: ${product3.tagColor03};display: inline-block;height:20px;line-height:20px;margin: 0px 5px 2px 0px;font-size: 16px;padding:0 3px;">
                		<#if product3.tagurl03 ??>
                		<img style="float: left;height:20px;margin-right: 0px;" src="${product3.tagurl03}" />
                		</#if>
                		${product3.tag03}
                		</div>
                	</#if>
                </div>
                <div style="width:225px;height:35px;margin-top:5px;">
                    <div style="font-size:25px;max-width:190px;white-space:nowrap;white-space: nowrap;overflow:hidden;color:#e02e24;font-weight:700;">
                        <span style="display: inline-block;color:#e02e24;font-size: 18px;margin-right: 1px;">¥</span>${product3.price}
                        <span style="display: inline-block;color:#9c9c9c;font-size: 16px;font-weight:400;">${product3.salesTip}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div style="float:left;width:250px;height:725px;">
        <div style="width:250px;height:360px;background-color: #fff;margin-bottom: 5px;">
            <div style="width:250px;height:250px;position: relative;background-color: #fafafa;">
                <div style="width:250px;height:250px;">
                    <img style="background-color:#fff;width:250px;height:250px;" src="${product2.goodsImg}">
                </div>
                <#if product2.mallName ?? &&  product2.mallStyle=="1">
                	<div style="background-color:#eaca93;color: #572600!important;position:absolute;bottom:0;left:12px;padding:0 5px;border-bottom-left-radius:0;border-bottom-right-radius:0;max-width:218px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
	                    ${product2.mallName}
	                </div>
                </#if>
                <#if product2.mallName ?? &&  product2.mallStyle=="0">
                	<div style="background:#FFF;color:#666;position:absolute;bottom:0;left:12px;padding:0 5px;border-bottom-left-radius:0;border-bottom-right-radius:0;max-width:218px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
	                    ${product2.mallName}
	                </div>
                </#if>
            </div>
            <div style="margin:10px;">
                <div style="width:225px;height:25px;line-height:25px;margin-bottom:4px;font-size:16px;color:#151516;word-wrap:normal;word-break:keep-all;overflow: hidden;">
                    <#if product2.prefixIcons01 ??>
                         <img style="float: left;height:22px;line-height:22px;padding:2px 0px;top:0px;margin-right: 6px;" src="${product2.prefixIcons01}">
                    </#if>
                    <#if product2.prefixIcons02 ??>
                         <img style="float: left;height:22px;line-height:22px;padding:2px 0px;top:0px;margin-right: 6px;" src="${product2.prefixIcons02}">
                    </#if>
                    <#if product2.prefixIcons03 ??>
                         <img style="float: left;height:22px;line-height:22px;padding:2px 0px;top:0px;margin-right: 6px;" src="${product2.prefixIcons03}">
                    </#if>
                    <div style="float: left;position:relative;overflow: hidden;">
                         <div style="width:${product2.goodsNameWith};position:absolute;top:0px;left:0px;right:10px;height:25px;line-height:25px;overflow: hidden;">${product2.goodsName}</div>
                    </div>
                </div>
                <div style="width:225px;height:20px;margin:5px 0px;white-space: nowrap;overflow:hidden;">
                	<#if product2.tag01 ??>
                		<div style="background: ${product2.tagBackgroudColor01}; color: ${product2.tagColor01};display: inline-block;height:20px;line-height:20px;margin: 0px 5px 2px 0px;font-size: 16px;padding:0 3px;">
                		<#if product2.tagurl01 ??>
                		<img style="float: left;height:20px;margin-right: 0px;" src="${product2.tagurl01}" />
                		</#if>
                		${product2.tag01}
                		</div>
                	</#if>
                   <#if product2.tag02 ??>
                		<div style="background:${product2.tagBackgroudColor02}; color: ${product2.tagColor02};display: inline-block;height:20px;line-height:20px;margin: 0px 5px 2px 0px;font-size: 16px;padding:0 3px;">
                		<#if product2.tagurl02 ??>
                		<img style="float: left;height:20px;margin-right: 0px;" src="${product2.tagurl02}" />
                		</#if>
                		${product2.tag02}
                		</div>
                	</#if>
                	<#if product2.tag03 ??>
                		<div style="background:${product2.tagBackgroudColor03}; color: ${product2.tagColor03};display: inline-block;height:20px;line-height:20px;margin: 0px 5px 2px 0px;font-size: 16px;padding:0 3px;">
                		<#if product2.tagurl03 ??>
                		<img style="float: left;height:20px;margin-right: 0px;" src="${product2.tagurl03}" />
                		</#if>
                		${product2.tag03}
                		</div>
                	</#if>
                </div>
                <div style="width:225px;height:35px;margin-top:5px;">
                    <div style="font-size:25px;max-width:190px;white-space:nowrap;white-space: nowrap;overflow:hidden;color:#e02e24;font-weight:700;">
                        <span style="display: inline-block;color:#e02e24;font-size: 18px;margin-right: 1px;">¥</span>${product2.price}
                        <span style="display: inline-block;color:#9c9c9c;font-size: 16px;font-weight:400;">${product2.salesTip}</span>
                    </div>
                </div>
            </div>
        </div>
        <div style="width:250px;height:360px;background-color: #fff;margin-bottom: 5px;">
            <div style="width:250px;height:250px;position: relative;background-color: #fafafa;">
                <div style="width:250px;height:250px;">
                    <img style="background-color:#fff;width:250px;height:250px;" src="${product4.goodsImg}">
                </div>
                <#if product4.mallName ?? &&  product4.mallStyle=="1">
                	<div style="background-color:#eaca93;color: #572600!important;position:absolute;bottom:0;left:12px;padding:0 5px;border-bottom-left-radius:0;border-bottom-right-radius:0;max-width:218px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
	                    ${product4.mallName}
	                </div>
                </#if>
                <#if product4.mallName ?? &&  product4.mallStyle=="0">
                	<div style="background:#FFF;color:#666;position:absolute;bottom:0;left:12px;padding:0 5px;border-bottom-left-radius:0;border-bottom-right-radius:0;max-width:218px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">
	                    ${product4.mallName}
	                </div>
                </#if>
            </div>
            <div style="margin:10px;">
                <div style="width:225px;height:25px;line-height:25px;margin-bottom:4px;font-size:16px;color:#151516;word-wrap:normal;word-break:keep-all;overflow: hidden;">
                    <#if product4.prefixIcons01 ??>
                         <img style="float: left;height:22px;line-height:22px;padding:2px 0px;top:0px;margin-right: 6px;" src="${product4.prefixIcons01}">
                    </#if>
                    <#if product4.prefixIcons02 ??>
                         <img style="float: left;height:22px;line-height:22px;padding:2px 0px;top:0px;margin-right: 6px;" src="${product4.prefixIcons02}">
                    </#if>
                    <#if product4.prefixIcons03 ??>
                         <img style="float: left;height:22px;line-height:22px;padding:2px 0px;top:0px;margin-right: 6px;" src="${product4.prefixIcons03}">
                    </#if>
                    <div style="float: left;position:relative;overflow: hidden;">
                         <div style="width:${product4.goodsNameWith};position:absolute;top:0px;left:0px;right:10px;height:25px;line-height:25px;overflow: hidden;">${product4.goodsName}</div>
                    </div>
                </div>
                <div style="width:225px;height:20px;margin:5px 0px;white-space: nowrap;overflow:hidden;">
                	<#if product4.tag01 ??>
                		<div style="background: ${product4.tagBackgroudColor01}; color: ${product4.tagColor01};display: inline-block;height:20px;line-height:20px;margin: 0px 5px 2px 0px;font-size: 16px;padding:0 3px;">
                		<#if product4.tagurl01 ??>
                		<img style="float: left;height:20px;margin-right: 0px;" src="${product4.tagurl01}" />
                		</#if>
                		${product4.tag01}
                		</div>
                	</#if>
                   <#if product4.tag02 ??>
                		<div style="background:${product4.tagBackgroudColor02}; color: ${product4.tagColor02};display: inline-block;height:20px;line-height:20px;margin: 0px 5px 2px 0px;font-size: 16px;padding:0 3px;">
                		<#if product4.tagurl02 ??>
                		<img style="float: left;height:20px;margin-right: 0px;" src="${product4.tagurl02}" />
                		</#if>
                		${product4.tag02}
                		</div>
                	</#if>
                	<#if product4.tag03 ??>
                		<div style="background:${product4.tagBackgroudColor03}; color: ${product4.tagColor03};display: inline-block;height:20px;line-height:20px;margin: 0px 5px 2px 0px;font-size: 16px;padding:0 3px;">
                		<#if product4.tagurl03 ??>
                		<img style="float: left;height:20px;margin-right: 0px;" src="${product4.tagurl03}" />
                		</#if>
                		${product4.tag03}
                		</div>
                	</#if>
                </div>
                <div style="width:225px;height:35px;margin-top:5px;">
                    <div style="font-size:25px;max-width:190px;white-space:nowrap;white-space: nowrap;overflow:hidden;color:#e02e24;font-weight:700;">
                        <span style="display: inline-block;color:#e02e24;font-size: 18px;margin-right: 1px;">¥</span>${product4.price}
                        <span style="display: inline-block;color:#9c9c9c;font-size: 16px;font-weight:400;">${product4.salesTip}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>