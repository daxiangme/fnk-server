<#assign entityName = entity?replace("DO", "")>
<#if controllerMappingHyphenStyle??>
  <#assign controllerPath = controllerMappingHyphen?replace("-","/")?replace("_","/")>
<#else>
  <#assign controllerPath = table.entityPath?replace("-","/")?replace("_","/")>
</#if>
<#assign apiPath = "/" + controllerPath>
<#if package.ModuleName?? && package.ModuleName != "">
  <#assign apiPath = "/" + package.ModuleName + apiPath>
</#if>
import request from '@/utils/http'

export interface ${entityName}Record {
  id?: string
<#list table.fields as field>
<#if !field.keyFlag>
<#if field.propertyType == "Boolean" || field.propertyType == "boolean">
  ${field.propertyName}?: boolean
<#elseif field.propertyType == "Integer" || field.propertyType == "Long" || field.propertyType == "Float" || field.propertyType == "Double">
  ${field.propertyName}?: number
<#else>
  ${field.propertyName}?: string
</#if>
</#if>
</#list>
}

export interface ${entityName}Query {
  page: number
  pageSize: number
<#list table.fields as field>
<#if !field.keyFlag>
<#if field.propertyType == "Boolean" || field.propertyType == "boolean">
  ${field.propertyName}?: boolean
<#elseif field.propertyType == "Integer" || field.propertyType == "Long" || field.propertyType == "Float" || field.propertyType == "Double">
  ${field.propertyName}?: number
<#else>
  ${field.propertyName}?: string
</#if>
</#if>
</#list>
}

export interface ${entityName}Form {
  id?: string
<#list table.fields as field>
<#if !field.keyFlag>
<#if field.propertyType == "Boolean" || field.propertyType == "boolean">
  ${field.propertyName}: boolean
<#elseif field.propertyType == "Integer" || field.propertyType == "Long" || field.propertyType == "Float" || field.propertyType == "Double">
  ${field.propertyName}: number
<#else>
  ${field.propertyName}: string
</#if>
</#if>
</#list>
}

export interface ${entityName}Page {
  records: ${entityName}Record[]
  total: number
}

const basePath = '${apiPath}'

export function fetchGet${entityName}List(params: ${entityName}Query) {
  return request.get<${entityName}Page>({
    url: basePath,
    params
  })
}

export function fetchSave${entityName}(data: ${entityName}Form) {
  if (data.id) {
    return request.put<${entityName}Record>({
      url: basePath + '/' + data.id,
      data
    })
  }

  return request.post<${entityName}Record>({
    url: basePath,
    data
  })
}

export function fetchDelete${entityName}(id: string) {
  return request.del<void>({
    url: basePath + '/' + id
  })
}
