import React from "react";


export function DownloadIndicator() {
  return (
    <div
      className="animate-bounce bg-white dark:bg-slate-800 p-2 w-8 h-8 ring-1 ring-slate-900/5
        dark:ring-slate-200/20 shadow-lg rounded-full flex items-center justify-center delay-150">
      <svg className="w-4 h-4 text-blue-500" fill="none" strokeLinecap="round"
           strokeLinejoin="round" strokeWidth="2" viewBox="0 0 24 24"
           stroke="currentColor">
        <path d="M19 14l-7 7m0 0l-7-7m7 7V3"></path>
      </svg>
    </div>
  );
}