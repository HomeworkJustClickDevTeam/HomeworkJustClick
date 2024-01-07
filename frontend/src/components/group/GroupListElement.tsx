import { colorsArray } from "../../assets/colors"
import { GroupInterface } from "../../types/GroupInterface"

function GroupListElement({ group }: { group: GroupInterface, key: number }) {
  return (
    <a className={`relative w-44 xl:w-64 ${colorsArray[group.color]}  min-h-fit h-28 xl:h-40 overflow-hidden rounded-md`} href={"/group/" + group.id}>
      <a className='w-full'>
        <h1
          className='flex items-center justify-center text-center pb-2 h-[50%] pt-3 text-white font-semibold text-sm xl:text-lg'>{group.name}</h1>{" "}
      </a>
      <p
        className='px-2 pl-2 flex items-center text-xs xl:text-base h-[50%] text-ellipsis bg-white bottom-0 text-black'>{group.description}</p>
    </a>
  )
}

export default GroupListElement
