<#escape x as x?xml>
<#if content.title??>
  <h3>${content.title }</h3>
</#if>
<#if image??>
<div class="image">
  <p class="image ${image.horizontalPosition }">
    <img src="${image.link}" alt="${image.alt}"  title="${content.image.alt }" />
    <#if image.caption??>
      <span>${image.caption}</span>
    </#if>
  </p>
  <#if html??>
    <#noescape>${html}</#noescape>
  </#if>
</div>
  
<#else>
<#if html??>
    <#noescape>${html}</#noescape>
  </#if>
</#if>
</#escape>

