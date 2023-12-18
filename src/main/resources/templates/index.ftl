<!DOCTYPE html>
<html>
<head>
    <title>fromZeroToExpert</title>
</head>
<body>
<h1>
    <#if msg??>
        ${msg}
    <#else>
        msg为空
    </#if>

</h1>

<br>
<h3>今日访问量： pv:${pv} ip:${ip} uv:${uv}</h3>
<br>
<h3>昨日访问量： pv:${yesterdayPv} ip:${yesterdayIp} uv:${yesterdayUv}</h3>


</body>
</html>
