<#-- CSV -->
<#if properties.bb_cycle?? >
	<#assign bb_cycle = properties.bb_cycle?number >
<#else>
	<#assign bb_cycle = 1>	
</#if>
<#if properties.width?? >
	<#assign width = properties.width?number >
<#else>
	<#assign width = 1200 >	
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
	<#assign header = "layername,width,height,crs,bb,type">	
</#if>
<#assign WFSCount = 0 >
<#assign WCSCount = 0 >
<#assign LayerGroupCount = 0 >
<#macro setWFSCount >${StressTest.setWFSCount(StressTest.getWFSCount()+1)}</#macro>
<#macro setWCSCount >${StressTest.setWCSCount(StressTest.getWCSCount()+1)}</#macro>
<#macro setLayerGroupCount >${StressTest.setLayerGroupCount(StressTest.getLayerGroupCount()+1)}</#macro>
<#-- Print header -->
${header}
<#-- For each layer in getCapability -->
<#list doc.WMS_Capabilities.Capability.Layer.Layer as Layer>
	<#-- Get the type using keywords -->
	<#list Layer.KeywordList.Keyword as k>
		<#if k=="features">
			<#assign type = "WFS" >
			<@setWFSCount/>
		<#elseif k=="WCS">
			<#assign type = "WCS" >
			<@setWCSCount/>
		<#else>
			<#assign type = "LayerGroup" >
			<@setLayerGroupCount/>	
		</#if>
	</#list>
	<#-- Randomize by width and height -->
	<#assign rwidth = StressTest.rand(10,width) >
	<#assign rheight = StressTest.rand(10,height) >
	<#list 1..wh_cycle as j>
		<#-- Search for bounding box -->
		<#list Layer.BoundingBox as BB>
			<#if BB.@CRS?matches("EPSG:.+")>
				<#assign bb = BB >
			</#if>
		</#list>
		<#list 1..bb_cycle as i>
		<#-- Randomize by bounding box -->
			<#assign bbox = bb.@CRS >
			<#assign minx = StressTest.rand(bb.@minx?number,bb.@maxx?number) >
			<#assign miny = StressTest.rand(bb.@miny?number,bb.@maxy?number) >
			<#assign maxx = StressTest.rand(minx,bb.@maxx?number) >
			<#assign maxy = StressTest.rand(miny,bb.@maxy?number) >	
${Layer.Name}${separator}${rwidth?floor}${separator}${rheight?floor}${separator}${bbox}${separator}${minx},${miny},${maxx},${maxy}${separator}${type}
		</#list>
	</#list>
</#list>
