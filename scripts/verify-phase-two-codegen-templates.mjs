import fs from 'node:fs'
import path from 'node:path'

const root = path.resolve(new URL('..', import.meta.url).pathname)

function read(relativePath) {
  return fs.readFileSync(path.join(root, relativePath), 'utf8')
}

function assert(condition, message) {
  if (!condition) {
    throw new Error(message)
  }
}

const pageTemplate = read('service-code-generate/src/main/resources/templates/vue-page.ftl')
const drawerTemplate = read('service-code-generate/src/main/resources/templates/vue-drawer.ftl')
const generator = read('service-code-generate/src/main/java/com/fnk/code/generate/CodeGenerate.java')
const apiTemplatePath = path.join(
  root,
  'service-code-generate/src/main/resources/templates/vue-api.ftl'
)

for (const oldToken of ['BaseTable', 'BaseForm', 'n-drawer', 'n-drawer-content', 'Naive']) {
  assert(!pageTemplate.includes(oldToken), `vue-page.ftl must not include ${oldToken}.`)
  assert(!drawerTemplate.includes(oldToken), `vue-drawer.ftl must not include ${oldToken}.`)
}

for (const token of ['ArtSearchBar', 'ArtTableHeader', 'FnkTable', 'ElDialog', 'useCrudTable', 'v-auth']) {
  assert(pageTemplate.includes(token), `vue-page.ftl must include ${token}.`)
}

for (const token of ['ElDialog', 'ElForm', 'defineModel', 'FormRules']) {
  assert(drawerTemplate.includes(token), `vue-drawer.ftl must include ${token}.`)
}

assert(fs.existsSync(apiTemplatePath), 'vue-api.ftl must exist.')
const apiTemplate = read('service-code-generate/src/main/resources/templates/vue-api.ftl')
for (const token of ['request from', 'fetchGet', 'fetchSave', 'fetchDelete']) {
  assert(apiTemplate.includes(token), `vue-api.ftl must include ${token}.`)
}

assert(generator.includes('api.ts'), 'CodeGenerate must emit api.ts custom file.')

console.log('phase-two M4 codegen template checks passed')
