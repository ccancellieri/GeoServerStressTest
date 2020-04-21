const { parse, transforms: { unwind } } = require('node_modules/json2csv/bin/json2csv');
var fs = require('fs');
const { cap } = JSON.parse(fs.readFileSync('./getCapabilities.json.txt', 'utf8'));

const fields = ['LayerName', 'BoundingBox'];
const transforms = [unwind([' WMS_Capabilities.Capability.Layer.Layer', 'WMS_Capabilities.Capability.Layer.Layer.BoundingBox']
)];
const json2csvParser = new parse({ fields, transforms });
const csv = json2csvParser.parse(cap);
/**
WMS_Capabilities.Capability.Layer.Layer.Name,
WMS_Capabilities.Capability.Layer.Layer.CRS,
WMS_Capabilities.Capability.Layer.Layer.BoundingBox,
WMS_Capabilities.Capability.Layer.Layer.Dimension.-name
,WMS_Capabilities.Capability.Layer.Layer.Name
 **/
console.log(csv);