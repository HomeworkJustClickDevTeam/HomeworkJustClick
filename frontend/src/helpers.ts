import exp from "constants"

export function colorToRGBA(color:string) {
  let cvs, ctx;
  cvs = document.createElement('canvas');
  cvs.height = 1;
  cvs.width = 1;
  ctx = cvs.getContext('2d');
  if(ctx !== null){
    ctx.fillStyle = color;
    ctx.fillRect(0, 0, 1, 1);
    return ctx.getImageData(0, 0, 1, 1).data;
  }
  else{
    return new Uint8ClampedArray()
  }

}

export function byteToHex(num:number) {
  return ('0'+num.toString(16)).slice(-2);
}

export function colorToHex(color:string) {
  let rgba:Uint8ClampedArray, hex:string;
  rgba = colorToRGBA(color);
  hex = [0,1,2].map(
    function(idx) { return byteToHex(rgba[idx]); }
  ).join('');
  return "#"+hex;
}