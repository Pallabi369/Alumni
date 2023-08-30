type ErrorPageFallbackProps = {
    error: any
}

export default function ErrorPageFallback(props: ErrorPageFallbackProps) {

    const code = props.error.response ? props.error.response.status : '5xx'
    const title = props.error.name
    const description = props.error.response && props.error.response.data !== '' ? props.error.response.data : props.error.message

    return (
        <div className="grid place-items-center h-screen max-w-md mx-auto">
            <div className="flex">
                <h3 className="flex-none text-blue-800 text-5xl font-bold px-4 hidden sm:block">{code}</h3>
                <div className="flex-1 grow">
                    <div className="border-l-2 border-gray-200 px-4">
                        <h2 className="font-bold text-5xl text-gray-900">{title}</h2>
                        <p className="text-gray-400 mt-4">{description}</p>
                    </div>
                </div>
            </div>
        </div>
    );
}
