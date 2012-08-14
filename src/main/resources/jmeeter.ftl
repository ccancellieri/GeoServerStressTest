<#-- CSV -->
<#if properties.bb_cycle?? >
	<#assign bb_cycle = properties.bb_cycle?number >
<#else>
	<#assign bb_cycle = 1>	
</#if>
<#if properties.weight?? >
	<#assign weight = properties.weight?number >
<#else>
	<#assign weight = 1200 >	
</#if>
<#if properties.height?? >
	<#assign height = properties.height?number >
<#else>
	<#assign height = 800 >	
</#if>
<#if properties.wh_cycle?? >
	<#assign wh_cycle = properties.wh_cycle?number >
<#else>
	<#assign wh_cycle = 10 >	
</#if>
<#if properties.separator?? >
	<#assign separator = properties.separator >
<#else>
	<#assign separator = ";">	
</#if>
<#if properties.header?? >
	<#assign header = properties.header >
<#else>
	<#assign header = "layername,weight,height,crs,bb,type">	
</#if>
${header}
<#list doc.WMS_Capabilities.Capability.Layer.Layer as Layer>
	<#list 1..wh_cycle as j>
		<#list 1..bb_cycle as i>
			<#list Layer.BoundingBox as bb>
				<#if bb.@CRS?matches("EPSG:.+")>
				<#assign bb = bb.@CRS >
				<#assign minx = StressTest.rand(bb.@minx?number,bb.@maxx?number) >
				<#assign miny = StressTest.rand(bb.@miny?number,bb.@maxy?number) >
				<#assign maxx = StressTest.rand(minx,bb.@maxx?number) >
				<#assign maxy = StressTest.rand(miny,bb.@maxy?number) >
				</#if>
			</#list>
			<#list Layer.KeywordList.Keyword as k>
				<#if k=="features">
					<#assign type = "WFS" >
				<#elseif k=="WCS">
					<#assign type = "WCS" >
				<#else>
					<#assign type = "LayerGroup" >	
				</#if>
			</#list>
${Layer.Name}${separator}${StressTest.rand(10,weight)?c}${separator}${StressTest.rand(10,height)?c}${separator}${bb}${separator}${minx},${miny},${maxx},${maxy}${separator}${type}
		</#list>
	</#list>
</#list>
