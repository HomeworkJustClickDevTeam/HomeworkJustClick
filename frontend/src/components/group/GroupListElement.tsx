
import {colorsArray} from "../../assets/colors";
import {GroupListElementPropsInterface} from "../../types/GroupListElementPropsInterface";

function GroupListElement({ group }: GroupListElementPropsInterface) {
  return (
    <div className={`relative w-64 ${colorsArray[group.color]}  min-h-fit h-40 overflow-hidden rounded-md`}>
      <a className='w-full' href={"/group/" + group.id}>
        <h1 className='flex items-center justify-center text-center pb-2 h-[50%] pt-3 text-white font-semibold text-lg'>{group.name}</h1>{" "}
      </a>
      <p className='px-2 pl-2 flex items-center text-base h-[50%] text-ellipsis bg-white bottom-0 text-black'>{group.description}</p>
    </div>
  )
}
export default GroupListElement
