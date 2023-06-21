import Logo from './Logo.svg';

function HeaderLogout(){
    return(
        <div className='relative flex h-[32px] xl:h-[64px] bg-main_blue items-center justify-center'>
            <img className="scale-50 fixed top-1 translate-y-[-25%] xl:top-2  xl:scale-100 xl:transform-none" src = {Logo} alt="Logo"></img>
        </div>
    )
}
export default HeaderLogout