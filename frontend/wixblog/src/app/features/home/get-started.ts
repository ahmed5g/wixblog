import { Component } from '@angular/core';

@Component({
  selector: 'app-get-started',
  imports: [],
  template: `
    <div class="bg-gray-100">
      <header class="bg-white shadow">
        <div class="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
          <h1 class="text-3xl font-bold text-gray-900 text-center">
            Medium
          </h1>
        </div>
      </header>

      <main>
        <div class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
          <div class="px-4 py-6 sm:px-0">
            <div class="border-4 border-dashed border-gray-200 rounded-lg h-full">
              <div class="text-center">
                <h2 class="text-4xl font-extrabold text-gray-900 tracking-tight sm:text-5xl sm:tracking-tight">
                  Human stories & ideas
                </h2>
                <p class="mt-3 text-base text-gray-500 sm:mt-5 sm:text-lg sm:max-w-xl sm:mx-auto md:mt-5 md:text-xl lg:mx-0">
                  A place to read, write, and deepen your understanding
                </p>
                <div class="mt-5 sm:mt-8 sm:flex sm:justify-center">
                  <div class="rounded-md shadow">
                    <a href="#" class="w-full flex items-center justify-center px-8 py-3 border border-transparent text-base font-medium rounded-md text-white bg-gray-900 hover:bg-gray-800 md:py-4 md:text-lg md:px-10">
                      Start reading
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  `,
  styles: ``,
})
export class GetStarted {

}
